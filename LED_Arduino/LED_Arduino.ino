//Pins für einzelne LEDs definieren:
const int led1 = 8;
const int led2 = 9;
const int led3 = 10;
const int led4 = 11;

String inputString = "";              // a string to hold incoming data
boolean stringComplete = false;       // whether the string is complete

void setup(){
 //LEDs als OUTPUT definieren:
 pinMode(led1, OUTPUT);
 pinMode(led2, OUTPUT);
 pinMode(led3, OUTPUT);
 pinMode(led4, OUTPUT);

//Serial-Schnittstelle
  Serial.begin(9600);
  Serial.println("################################################################################");
  Serial.println("\t \t \t LED Controller v1.0");
  Serial.println("\t Author: Kevin Pan, Alexander Wurm");
  Serial.println("\t Last change: 13.02.2015");
  Serial.println("\t Contact: kpan@student.tgm.ac.at, awurm@student.tgm.ac.at");
  Serial.println("\t JAVA-GUI: Kevin Pan");
  Serial.println("\t Arduino-Sketch: Alexander Wurm");
  Serial.println("################################################################################");
  Serial.println("SYSTEM: Connection to JAVA GUI ...");
  _init();
  Serial.println("SYSTEM: Ready and waiting for commands ...");
}

void loop(){
if (stringComplete) {
    String a=inputString.substring(1,2);
    String b=inputString.substring(3,4);
    String c=inputString.substring(5,6);
    String d=inputString.substring(7,8);
   
    Serial.print(a);
    Serial.print(b);
    Serial.print(c);
    Serial.print(d);
    Serial.println();
    
    if(a=="1"){
      digitalWrite(led1, HIGH);
    }else{
      digitalWrite(led1, LOW);
    }
    if(b=="1"){
      digitalWrite(led2, HIGH);
    }else{
      digitalWrite(led2, LOW);
    }
    
    if(c=="1"){
      digitalWrite(led3, HIGH);
    }else{
      digitalWrite(led3, LOW);
    }
    
    if(d=="1"){
      digitalWrite(led4, HIGH);
    }else{
      digitalWrite(led4, LOW);
    }
    
        // clear the string:
    inputString = "";
    stringComplete = false;
  }
}

void _init(){                  //Initailizing Pin setups
    inputString.reserve(200);  //reserve 200 bytes for the inputString:
}

void serialEvent() {
  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read();
    // add it to the inputString:
    inputString += inChar;
    // if the incoming character is a newline, set a flag
    // so the main loop can do something about it:
    if (inChar == '#') {
      stringComplete = true;
    }
  }
}
