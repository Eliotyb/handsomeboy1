import os
import requests
import time
import random
from urllib.parse import quote

SAVE_DIR = r"D:\warcraft"
KEYWORD = "魔兽世界"
TARGET_COUNT = 30
PAGE_SIZE = 24

HEADERS = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
    "Referer": "https://www.duitang.com/",
    "Accept": "application/json, text/plain, */*",
}

PROXIES = {"http": None, "https": None}


def ensure_dir(path):
    if not os.path.exists(path):
        os.makedirs(path)
        print(f"[OK] 已创建文件夹: {path}")
    else:
        print(f"[OK] 文件夹已存在: {path}")


def fetch_image_urls(keyword, target_count):
    urls = []
    start = 0
    page = 1

    print(f"\n[搜索] 关键词: \"{keyword}\" | 目标: {target_count} 张")

    while len(urls) < target_count:
        api_url = (
            f"https://www.duitang.com/napi/blog/list/by_search/"
            f"?kw={quote(keyword)}&start={start}"
        )

        try:
            resp = requests.get(api_url, headers=HEADERS, proxies=PROXIES, timeout=15)
            resp.raise_for_status()
            data = resp.json()

            if data.get("status") != 1 or "data" not in data:
                print(f"[警告] 第{page}页返回异常: {data.get('message', '未知错误')}")
                break

            items = data["data"].get("object_list", [])
            if not items:
                print(f"[信息] 第{page}页无更多数据，搜索结束")
                break

            for item in items:
                photo = item.get("photo", {})
                img_path = photo.get("path", "") if isinstance(photo, dict) else ""

                if not img_path:
                    continue

                if not img_path.startswith("http"):
                    img_url = "https:" + img_path
                else:
                    img_url = img_path

                urls.append(img_url)

                if len(urls) >= target_count:
                    break

            fetched_this = len(items)
            print(f"  第{page}页: 获取 {fetched_this} 条 | 累计: {len(urls)}/{target_count}")

            start += PAGE_SIZE
            page += 1
            time.sleep(random.uniform(0.5, 1.2))

        except requests.exceptions.RequestException as e:
            print(f"[错误] 请求失败(第{page}页): {e}")
            break
        except Exception as e:
            print(f"[错误] 解析异常: {e}")
            break

    return urls[:target_count]


def download_image(url, save_path, index, total):
    try:
        resp = requests.get(url, headers=HEADERS, proxies=PROXIES, timeout=20)
        resp.raise_for_status()

        content_type = resp.headers.get("Content-Type", "")
        if "image" not in content_type and len(resp.content) < 5000:
            print(f"  [跳过] #{index}: 非图片内容 (Content-Type: {content_type})")
            return False

        with open(save_path, "wb") as f:
            f.write(resp.content)

        size_kb = len(resp.content) / 1024
        print(f"  [下载] #{index}/{total}: {os.path.basename(save_path)} ({size_kb:.1f} KB)")
        return True

    except requests.exceptions.RequestException as e:
        print(f"  [失败] #{index}: 下载失败 - {e}")
        return False
    except Exception as e:
        print(f"  [失败] #{index}: {e}")
        return False


def main():
    print("=" * 50)
    print("  堆糖网图片爬虫 - 魔兽世界")
    print("=" * 50)

    ensure_dir(SAVE_DIR)

    print("\n[步骤1] 搜索图片链接...")
    image_urls = fetch_image_urls(KEYWORD, TARGET_COUNT)

    if not image_urls:
        print("\n[结果] 未获取到任何图片链接，请检查网络或关键词")
        return

    print(f"\n[步骤2] 开始下载 {len(image_urls)} 张图片到: {SAVE_DIR}")

    success = 0
    failed = 0

    for i, url in enumerate(image_urls, 1):
        filename = f"{i:03d}.jpg"
        save_path = os.path.join(SAVE_DIR, filename)

        if download_image(url, save_path, i, len(image_urls)):
            success += 1
        else:
            failed += 1

        if i < len(image_urls):
            time.sleep(random.uniform(0.3, 0.8))

    print("\n" + "=" * 50)
    print(f"  下载完成! 成功: {success} 张 | 失败: {failed} 张")
    print(f"  保存路径: {SAVE_DIR}")
    print("=" * 50)


if __name__ == "__main__":
    main()
