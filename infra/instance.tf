data "aws_ami" "amazon_linux" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["al2023-ami-2023.*-x86_64"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

data "aws_iam_instance_profile" "lab" {
  name = var.instance_profile_name
}

locals {
  corretto_url    = "https://corretto.aws/downloads/resources/25.0.5.1.2/amazon-corretto-25.0.5.1.2-linux-x64.tar.gz"
  corretto_sha256 = "8d2be3f035dbdb7f7475e2f7009b7dc20683bfc351ad41064218c8de596c9644"
  ttyd_url        = "https://github.com/tsl0922/ttyd/releases/download/1.7.7/ttyd.x86_64"
  ttyd_sha256     = "8a217c968aba172e0dbf3f34447218dc015bc4d5e59bf51db2f2cd12b7be4f55"

  fetch_script = templatefile("${path.module}/templates/fetch.sh.tftpl", {
    bucket = local.artifact_bucket
    region = var.aws_region
  })

  unit_file = file("${path.module}/templates/tictactest.service")
}

resource "aws_instance" "web" {
  ami           = data.aws_ami.amazon_linux.id
  instance_type = var.instance_type
  subnet_id     = data.aws_subnets.default.ids[0]
  key_name      = var.key_name
  vpc_security_group_ids = [aws_security_group.web.id]
  iam_instance_profile   = data.aws_iam_instance_profile.lab.name

  # A new release must not recreate this instance. The version lives in S3, not here.
  user_data_replace_on_change = false
  user_data = templatefile("${path.module}/templates/user-data.sh.tftpl", {
    corretto_url    = local.corretto_url
    corretto_sha256 = local.corretto_sha256
    ttyd_url        = local.ttyd_url
    ttyd_sha256     = local.ttyd_sha256
    fetch_b64       = base64encode(local.fetch_script)
    unit_b64        = base64encode(local.unit_file)
  })

  metadata_options {
    http_endpoint = "enabled"
    http_tokens   = "required"
  }

  tags = {
    Name = "tictactest"
  }
}
