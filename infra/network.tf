data "aws_vpc" "default" {
  default = true
}

data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }

  filter {
    name   = "default-for-az"
    values = ["true"]
  }
}

resource "aws_security_group" "web" {
  name_prefix = "tictactest-"
  description = "SSH nur von der eigenen Adresse, Spiel und Version aus dem Browser"
  vpc_id      = data.aws_vpc.default.id

  ingress {
    description = "SSH fuer Deploy und Fehlersuche, nur von ssh_cidr"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.ssh_cidr]
  }

  ingress {
    description = "ttyd. Der Browser des Spielers oeffnet das Terminal direkt"
    from_port   = 7681
    to_port     = 7681
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Versionsabfrage. Frontend und spaeter die Pipeline rufen sie auf"
    from_port   = 7682
    to_port     = 7682
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "HTTPS: Corretto, ttyd, S3 und SSM"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "DNS"
    from_port   = 53
    to_port     = 53
    protocol    = "udp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "tictactest"
  }
}

resource "aws_eip" "web" {
  domain = "vpc"

  tags = {
    Name = "tictactest"
  }
}

resource "aws_eip_association" "web" {
  instance_id   = aws_instance.web.id
  allocation_id = aws_eip.web.id
}
