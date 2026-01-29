import os

# ChromeDriver yolunu buraya ekleyin
# Eğer projenizin kök dizininde ise: os.path.join(os.getcwd(), "chromedriver.exe")
# Veya belirli bir yolsa: r"C:\WebDriver\chromedriver.exe"
CHROMEDRIVER_PATH = r"C:\WebDriver\chromedriver.exe" 

BASE_URL = "https://www.kitapyurdu.com/"

# ChromeDriver yolunun varlığını kontrol et
if not os.path.exists(CHROMEDRIVER_PATH):
    raise FileNotFoundError(f"ChromeDriver bulunamadı: {CHROMEDRIVER_PATH}")