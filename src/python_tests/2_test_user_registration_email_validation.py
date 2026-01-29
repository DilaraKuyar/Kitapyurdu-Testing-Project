import unittest
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time
import os
from config import CHROMEDRIVER_PATH, BASE_URL # BASE_URL burada artık doğrudan kullanılmayacak, ama config dosyasından import edilebilir.

class TestUserRegistrationEmailValidation(unittest.TestCase):

    def setUp(self):
        service = Service(executable_path=CHROMEDRIVER_PATH)
        self.driver = webdriver.Chrome(service=service)
        self.driver.maximize_window()
        # Doğrudan verilen "üye-ol" URL'sini kullanıyoruz
        self.driver.get("https://www.kitapyurdu.com/index.php?route=account/register") 
        
        # Email input'u ve password input'u yüklenene kadar bekleyelim
        WebDriverWait(self.driver, 10).until(
            EC.presence_of_element_located((By.ID, "email"))
        )
        self.email_input = self.driver.find_element(By.ID, "email")
        self.password_input = self.driver.find_element(By.ID, "password") # Validasyonu tetiklemek için
        
        # Email hata mesajı elementinin locator'ı
        self.email_error_msg_locator = (By.CSS_SELECTOR, "div[data-valid-id='email']") 


    def tearDown(self):
        self.driver.quit()

    def test_email_valid_format(self):
        print("\n--- Test Case 2: User Registration – Email Format Validation - Valid ---")
        self.email_input.clear() 
        self.email_input.send_keys("user@example.com")
        self.password_input.click() 
        time.sleep(0.5)
        
        try:
            WebDriverWait(self.driver, 2).until_not(
                EC.visibility_of_element_located(self.email_error_msg_locator)
            )
            print("PASS: Geçerli email kabul edildi, hata mesajı yok.")
        except Exception as e:
            self.fail(f"FAIL: Geçerli email için hata mesajı çıktı: {e}")
        print("Test Case 2: User Registration – Email Format Validation - Valid - BAŞARILI")


    def test_email_invalid_no_at_symbol(self):
        print("\n--- Test Case 2: User Registration – Email Format Validation - Invalid (No '@') ---")
        self.email_input.clear()
        self.email_input.send_keys("ilaydaexample.com")
        self.password_input.click() 
        time.sleep(1) 
        
        error_message_element = WebDriverWait(self.driver, 5).until(
            EC.visibility_of_element_located(self.email_error_msg_locator)
        )
        error_message = error_message_element.text
        self.assertIn("Geçerli bir E-Posta adresi yazınız!", error_message, "FAIL: Email format hatası mesajı bulunamadı veya farklı.")
        print("PASS: '@' sembolü eksik email için doğru hata mesajı gösterildi.")
        print("Test Case 2: User Registration – Email Format Validation - Invalid (No '@') - BAŞARILI")


    def test_email_invalid_no_domain(self):
        print("\n--- Test Case 2: User Registration – Email Format Validation - Invalid (No domain) ---")
        self.email_input.clear()
        self.email_input.send_keys("test@")
        self.password_input.click()
        time.sleep(1)

        error_message_element = WebDriverWait(self.driver, 5).until(
            EC.visibility_of_element_located(self.email_error_msg_locator)
        )
        error_message = error_message_element.text
        self.assertIn("Geçerli bir E-Posta adresi yazınız!", error_message, "FAIL: Email format hatası mesajı (domain eksik) bulunamadı.")
        print("PASS: Domain eksik email için doğru hata mesajı gösterildi.")
        print("Test Case 2: User Registration – Email Format Validation - Invalid (No domain) - BAŞARILI")


    def test_email_invalid_empty(self):
        print("\n--- Test Case 2: User Registration – Email Format Validation - Invalid (Empty) ---")
        self.email_input.clear() 
        self.password_input.click() 
        time.sleep(1)
        
        error_message_element = WebDriverWait(self.driver, 5).until(
            EC.visibility_of_element_located(self.email_error_msg_locator)
        )
        error_message = error_message_element.text
        self.assertIn("Bu alan zorunludur!", error_message, "FAIL: Boş email alanı hata mesajı bulunamadı veya farklı.")
        print("PASS: Boş email alanı için doğru hata mesajı gösterildi.")
        print("Test Case 2: User Registration – Email Format Validation - Invalid (Empty) - BAŞARILI")


if __name__ == "__main__":
    unittest.main()