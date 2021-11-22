#include <ESP8266WiFi.h>
#include <DHT.h>
#define DHTPIN 0   
#define DHTTYPE DHT11

DHT dht(DHTPIN, DHTTYPE);
void sendSensor()
{
  float h = dht.readHumidity();
  float t = dht.readTemperature(); 

  if (isnan(h) || isnan(t)) {
    Serial.println("Failed to read from DHT sensor!");
    return;
  }
  Serial.println(String(h)+"%");
  Serial.println(String(t)+"*C");
}
void setup()
{
  pinMode(2,OUTPUT);
  Serial.begin(115200);
  dht.begin();
 // timer.setInterval(1000L, sendSensor);
}

void loop()
{
  sendSensor();
  delay(1000);
}
