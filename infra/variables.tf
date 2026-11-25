variable "aws_region" {
  description = "Region des Learner Labs."
  type        = string
  default     = "us-east-1"
}

variable "instance_type" {
  description = "Kleine Instanz. t3.micro hat 1 GB RAM."
  type        = string
  default     = "t3.micro"
}

variable "key_name" {
  description = "Name des SSH-Schlüssels aus dem IaC-Auftrag. Den privaten Schlüssel nicht ins Repo legen."
  type        = string
}

variable "ssh_cidr" {
  description = "Einzige Adresse, die SSH darf, als CIDR, zum Beispiel 203.0.113.10/32."
  type        = string

  validation {
    condition     = can(cidrnetmask(var.ssh_cidr))
    error_message = "ssh_cidr muss ein CIDR sein, zum Beispiel 203.0.113.10/32."
  }
}

variable "instance_profile_name" {
  description = "Vorhandenes Lab-Profil. Es wird keine eigene IAM-Rolle angelegt."
  type        = string
  default     = "LabInstanceProfile"
}
