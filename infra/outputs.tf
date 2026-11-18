output "frontend_url" {
  description = "Oeffentliche Spielseite. Nicht vertraulich."
  value       = "http://${aws_s3_bucket_website_configuration.frontend.website_endpoint}"
}

output "console_url" {
  description = "Web-Konsole. Nicht vertraulich, steht auch in config.js."
  value       = "http://${aws_eip.web.public_ip}:7681"
}

output "version_url" {
  description = "Laufende Backend-Version. Nicht vertraulich."
  value       = "http://${aws_eip.web.public_ip}:7682/version"
}

output "public_ip" {
  description = "Feste Adresse. Nicht vertraulich."
  value       = aws_eip.web.public_ip
}

output "instance_id" {
  description = "Fuer den Neustart des Dienstes. Kein Geheimnis, aber nur fuer das Deployment."
  value       = aws_instance.web.id
}

output "artifact_bucket" {
  description = "Privater Bucket mit Jar und current. Kein Geheimnis, nicht veröffentlichen."
  value       = aws_s3_bucket.artifacts.bucket
}

output "frontend_bucket" {
  description = "Oeffentlicher Website-Bucket. Kein Geheimnis."
  value       = aws_s3_bucket.frontend.bucket
}

output "aws_region" {
  description = "Region fuer die AWS-CLI."
  value       = var.aws_region
}
