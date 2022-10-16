#include <MCreatorLink.h>
#include <Simpletimer.h>
#include <TM1637Display.h>

#define PAIN 6
#define CLK 7
#define DIO 5
TM1637Display display(CLK, DIO);

int timeout = 0;
Simpletimer countdown;

void dataEvent(String command, String data){
  timeout += data.toInt();
  digitalWrite(PAIN, HIGH);
}

void setup() {
  display.setBrightness(7);
  display.showNumberDec(0);
  Serial.begin(115200);
  Serial.setTimeout(20);
  MCreatorLink.setup(Serial, "PAINBOX");
  MCreatorLink.setListener(dataEvent);
  pinMode(PAIN, OUTPUT);
}

void loop() {
  MCreatorLink.loop();
  if (countdown.timer(167)){
    if (timeout > 0){
      timeout -= 1;
    }
    else {
      if (digitalRead(PAIN) == HIGH){
        digitalWrite(PAIN, LOW);
      }
    }
    display.showNumberDec(timeout);
  }
}