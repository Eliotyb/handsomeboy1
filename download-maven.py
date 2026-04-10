import urllib.request
import zipfile
import os

print("Downloading Maven...")
url = "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.8.6/apache-maven-3.8.6-bin.zip"
save_path = "apache-maven.zip"

try:
    urllib.request.urlretrieve(url, save_path)
    print("Download completed!")
    
    # Extract
    print("Extracting...")
    with zipfile.ZipFile(save_path, 'r') as zip_ref:
        zip_ref.extractall(".")
    print("Maven extracted successfully!")
    
except Exception as e:
    print(f"Error: {e}")
