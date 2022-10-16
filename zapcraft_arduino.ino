#include <MCreatorLink.h>
#include <Simpletimer.h>

#define PAIN 6 // Set this to the pin controlling your relay

int shockTime = 167; // Per half-heart of damage. Currently 1/6th of a second.
/*
  Do not underestimate just how long a second is. 
  Even a short blip will make itself known.
  Trust me...
*/


int timeout = 0;
Simpletimer countdown;

void dataEvent(String command, String data){
  timeout += data.toInt();
  digitalWrite(PAIN, HIGH);
}

void setup() {
  Serial.begin(115200);
  Serial.setTimeout(20);
  MCreatorLink.setup(Serial, "PAINBOX");
  MCreatorLink.setListener(dataEvent);
  pinMode(PAIN, OUTPUT);
}

void loop() {
  MCreatorLink.loop();
  if (countdown.timer(shockTime)){
    if (timeout > 0){
      timeout -= 1;
    }
    else {
      if (digitalRead(PAIN) == HIGH){
        digitalWrite(PAIN, LOW);
      }
    }
  }
}