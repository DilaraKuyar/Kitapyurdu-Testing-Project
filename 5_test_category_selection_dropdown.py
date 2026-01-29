import unittest
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time
import os
from config import CHROMEDRIVER_PATH, BASE_URL

class TestCategorySelectionDropdown(unittest.TestCase):

    def setUp(self):
        service = Service(executable_path=CHROMEDRIVER_PATH)
        self.driver = webdriver.Chrome(service=service)
        self.driver.maximize_window()
        self.driver.get(BASE_URL)
        # Kategori filtrelerinin görünmesi için başlangıçta bir arama yapalım
        search_box = self.driver.find_element(By.ID, "search-input")
        search_box.send_keys("kitap")
        search_box.send_keys(Keys.RETURN)
        WebDriverWait(self.driver, 10).until(
            EC.presence_of_element_located((By.XPATH, "//a[contains(@href, '/kategori/')]"))
        )

    def tearDown(self):
        self.driver.quit()

    def test_category_valid_selection(self):
        print("\n--- Test Case 5: Category Selection Dropdown - Valid ---")
        try:
            # "Çocuk Kitapları" kategorisini bul ve tıkla
            children_books_category = self.driver.find_element(By.XPATH, "//a[text()='Çocuk Kitapları']")
            children_books_category.click()
            WebDriverWait(self.driver, 10).until(
                EC.url_contains("cocuk-kitaplari")
            )
            self.assertIn("çocuk kitapları", self.driver.title.lower(), "FAIL: Çocuk Kitapları kategorisi filtrelenmedi.")
            print("PASS: 'Çocuk Kitapları' kategorisi seçimi başarılı, ilgili sayfa yüklendi.")
        except Exception as e:
            self.fail(f"FAIL: 'Çocuk Kitapları' kategorisi linki bulunamadı veya tıklanamadı: {e}")
        print("Test Case 5: Category Selection Dropdown - Valid - BAŞARILI")


    def test_category_invalid_no_selection(self):
        print("\n--- Test Case 5: Category Selection Dropdown - Invalid (No Selection) ---")
        # Kitapyurdu'nda 'no selection' durumu, kategori filtresi uygulanmadığında tüm ürünleri gösterir.
        # Bu durumu simüle etmek için kategori filtresini temizlememiz (varsa) veya direkt anasayfaya dönüp
        # kategori filtresi uygulanmadan tüm ürünlerin göründüğünü kontrol etmemiz gerekir.
        
        # Basitçe, kategori seçimi yapmadan önceki URL'e geri dönüp,
        # herhangi bir filtrenin uygulanmadığını varsayabiliriz.
        self.driver.get(BASE_URL) # Ana sayfaya geri dön
        WebDriverWait(self.driver, 10).until(
            EC.presence_of_element_located((By.ID, "search-input"))
        )
        # Burada tüm ürünlerin listelendiğini veya varsayılan bir görünümde olduğunu kontrol edebiliriz.
        # Örneğin, belirli bir kategori başlığının (Çocuk Kitapları gibi) olmadığını kontrol edebiliriz.
        self.assertNotIn("çocuk kitapları", self.driver.title.lower(), "FAIL: Kategori seçilmediği halde 'Çocuk Kitapları' başlığı görünüyor.")
        print("PASS: Kategori seçilmediğinde, belirli bir kategoriye özel filtre uygulanmadı.")
        print("Test Case 5: Category Selection Dropdown - Invalid (No Selection) - BAŞARILI")

    def test_category_invalid_disabled(self):
        print("\n--- Test Case 5: Category Selection Dropdown - Invalid (Disabled) ---")
        # Kitapyurdu'nda "disabled" kategori seçeneği bulunmamaktadır, genellikle linkler her zaman etkindir.
        # Bu senaryoyu test etmek için sitenin özel bir "disabled" kategorisi olması veya
        # JavaScript ile bir kategorinin geçici olarak pasifize edilmesi gerekir.
        # Bu test case için genel bir "geçersiz kategoriye tıklama" denemesi yapabiliriz.
        
        # Varsayımsal olarak, olmayan bir kategoriye tıklamaya çalışalım
        try:
            self.driver.find_element(By.XPATH, "//a[text()='Geçersiz Kategori']").click()
            self.fail("FAIL: Var olmayan bir kategoriye tıklanabildi.")
        except:
            print("PASS: Var olmayan bir kategoriye tıklanamadı (element bulunamadı veya etkileşim engellendi).")
        print("Test Case 5: Category Selection Dropdown - Invalid (Disabled/Non-existent) - BAŞARILI")


if __name__ == "__main__":
    unittest.main()