import os
import subprocess

backend_dir = r"C:\Users\杨彬\Desktop\社团\backend"
os.chdir(backend_dir)

# Run with shorter classpath using wildcard
classpath = "target/classes;C:\\Users\\杨彬\\.m2\\repository\\*\\*\\*.jar"

print("Starting Spring Boot application with wildcard classpath...")

subprocess.run([
    "java",
    "-cp",
    classpath,
    "com.community.groupon.GrouponApplication"
], shell=True)
