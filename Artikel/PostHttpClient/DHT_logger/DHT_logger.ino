/**
   PostHTTPClient.ino

    Created on: 21.11.2016

*/

#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>

#include <DHT.h>
#define DHTPIN 0   
#define DHTTYPE DHT11


/* this can be run with an emulated server on host:
        cd esp8266-core-root-dir
        cd tests/host
        make ../../libraries/ESP8266WebServer/examples/PostServer/PostServer
        bin/PostServer/PostServer
   then put your PC's IP address in SERVER_IP below, port 9080 (instead of default 80):
*/
//#define SERVER_IP "10.0.1.7:9080" // PC address with emulation on host
#define SERVER_NAME "192.168.1.29"

#ifndef STASSID
#define STASSID "KURNIAWAN_EL06"
#define STAPSK  "1234qwerty"
#endif

DHT dht(DHTPIN, DHTTYPE);

void setup() {

  Serial.begin(115200);
  dht.begin();

  Serial.println();
  Serial.println();
  Serial.println();

  WiFi.begin(STASSID, STAPSK);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected! IP address: ");
  Serial.println(WiFi.localIP());

}

void loop() {
  float h = dht.readHumidity(); // baca kelembaban dan tampung dalam variabel h
  float t = dht.readTemperature(); // baca suhu dan tampung dalam variabel t
  String postData;  // variabel untuk menyimpan data yg akan di POST
   if (isnan(h) || isnan(t)) {
    Serial.println("Failed to read from DHT sensor!");
    return;
  }
   postData = "t=" + String(int(t)) + "&h=" + String(int(h));

  if ((WiFi.status() == WL_CONNECTED)) {
    WiFiClient client;
    HTTPClient http;
    // start connection and send HTTP header and body
    Serial.print("[HTTP] begin...\n");
    http.begin(client, "http://" SERVER_NAME "/guest/kegiatan");
    http.addHeader("Content-Type", "application/x-www-form-urlencoded");
    Serial.println("[HTTP] POST... data:"+postData); // Tampilkan data yang akan dikirim
    int httpCode = http.POST(postData); // postData dikirim dan response disimpan di httpcode

    if (httpCode > 0) {
      Serial.printf("[HTTP] POST... code: %d\n", httpCode); //tampilkan HTTPresponse code
    }
    http.end();
  }
  delay(60000); // tunda 1 menit untuk pengulangan berikutnya
}
