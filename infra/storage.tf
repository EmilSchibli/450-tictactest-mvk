data "aws_caller_identity" "current" {}

locals {
  artifact_bucket = "tictactest-artifacts-${data.aws_caller_identity.current.account_id}"
  frontend_bucket = "tictactest-frontend-${data.aws_caller_identity.current.account_id}"
}

# The lab SCP denies s3:GetBucketObjectLockConfiguration. The aws_s3_bucket
# resource reads that on every plan, so the buckets are created with the CLI.
resource "terraform_data" "artifact_bucket" {
  input = local.artifact_bucket

  provisioner "local-exec" {
    when        = create
    interpreter = ["bash", "-lc"]
    command     = "aws s3api head-bucket --bucket ${self.input} --region ${var.aws_region} 2>/dev/null || aws s3api create-bucket --bucket ${self.input} --region ${var.aws_region} --create-bucket-configuration LocationConstraint=${var.aws_region}"
  }

  provisioner "local-exec" {
    when        = destroy
    interpreter = ["bash", "-lc"]
    command     = "aws s3 rb s3://${self.input} --force || true"
  }
}

resource "terraform_data" "frontend_bucket" {
  input = local.frontend_bucket

  provisioner "local-exec" {
    when        = create
    interpreter = ["bash", "-lc"]
    command     = "aws s3api head-bucket --bucket ${self.input} --region ${var.aws_region} 2>/dev/null || aws s3api create-bucket --bucket ${self.input} --region ${var.aws_region} --create-bucket-configuration LocationConstraint=${var.aws_region}"
  }

  provisioner "local-exec" {
    when        = destroy
    interpreter = ["bash", "-lc"]
    command     = "aws s3 rb s3://${self.input} --force || true"
  }
}

resource "aws_s3_bucket_public_access_block" "artifacts" {
  bucket                  = local.artifact_bucket
  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true

  depends_on = [terraform_data.artifact_bucket]
}

resource "aws_s3_bucket_public_access_block" "frontend" {
  bucket                  = local.frontend_bucket
  block_public_acls       = true
  block_public_policy     = false
  ignore_public_acls      = true
  restrict_public_buckets = false

  depends_on = [terraform_data.frontend_bucket]
}

resource "aws_s3_bucket_website_configuration" "frontend" {
  bucket = local.frontend_bucket

  index_document {
    suffix = "index.html"
  }

  depends_on = [terraform_data.frontend_bucket]
}

resource "aws_s3_bucket_policy" "frontend" {
  bucket = local.frontend_bucket
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Sid       = "PublicRead"
      Effect    = "Allow"
      Principal = "*"
      Action    = "s3:GetObject"
      Resource  = "arn:aws:s3:::${local.frontend_bucket}/*"
    }]
  })

  depends_on = [aws_s3_bucket_public_access_block.frontend]
}

resource "aws_s3_object" "config" {
  bucket       = local.frontend_bucket
  key          = "config.js"
  content_type = "application/javascript"
  content = templatefile("${path.module}/templates/config.js.tftpl", {
    public_ip = aws_eip.web.public_ip
  })

  depends_on = [aws_s3_bucket_policy.frontend]
}
