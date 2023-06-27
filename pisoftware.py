from firebase_admin import db
import pyrebase
import RPi.GPIO as GPIO

GPIO.setmode(GPIO.BCM)
GPIO.setup(26, GPIO.OUT)
GPIO.setup(6, GPIO.OUT)

def senddata():
    config={
        "apiKey": "AIzaSyCMdwsdzWycUH_WVbkG9RRfIEadWazLaIo",
        "authDomain": "homeautomation-ddfdd.firebaseapp.com",
        "databaseURL": "https://homeautomation-ddfdd-default-rtdb.firebaseio.com",
        "projectId": "homeautomation-ddfdd",
        "storageBucket": "homeautomation-ddfdd.appspot.com",
        "messagingSenderId": "587715575052",
        "appId": "1:587715575052:web:6e2d075cb4e846bd21dd29",
        "measurementId": "G-YYKQMGCPCG"
        }
    firebase= pyrebase.initialize_app(config)
    database=firebase.database()
    users=database.child("pi message").set("")
def getdata():
    config={
        "apiKey": "AIzaSyCMdwsdzWycUH_WVbkG9RRfIEadWazLaIo",
        "authDomain": "homeautomation-ddfdd.firebaseapp.com",
        "databaseURL": "https://homeautomation-ddfdd-default-rtdb.firebaseio.com",
        "projectId": "homeautomation-ddfdd",
        "storageBucket": "homeautomation-ddfdd.appspot.com",
        "messagingSenderId": "587715575052",
        "appId": "1:587715575052:web:6e2d075cb4e846bd21dd29",
        "measurementId": "G-YYKQMGCPCG"}
    firebase= pyrebase.initialize_app(config)
    database=firebase.database()
    getdata=database.child("pi message").get()
    msg=getdata.val()
    return msg
    
while True:
    c=getdata()
    #c=input("enter on and off:")
    #c="switch2 off"
    if "on" in c:
        if "switch1" in c:
            GPIO.output(6, GPIO.LOW)
            senddata()
        if "switch2" in c:
            GPIO.output(26, GPIO.LOW)
            senddata()
            print("switch2 on")
        if "switch3" in c:
            GPIO.output(5, GPIO.LOW)
            senddata()
            print("switch3 on")
        if "switch4" in c:
            GPIO.output(26, GPIO.LOW)
            senddata()
            print("switch4 on")
        if "switch5" in c:
            GPIO.output(26, GPIO.LOW)
            senddata()
            print("switch5 on")
        if "switch6" in c:
            GPIO.output(26, GPIO.LOW)
            senddata()
            print("switch6 on")
        if "switch7" in c:
            GPIO.output(26, GPIO.LOW)
            senddata()
            print("switch7 on")
        if "switch8" in c:
            GPIO.output(26, GPIO.LOW)
            senddata()
            print("switch8 on")
        if "everything" in c:
            GPIO.output(26, GPIO.LOW)
            GPIO.output(6, GPIO.LOW)
            GPIO.output(5, GPIO.LOW)
            GPIO.output(26, GPIO.LOW)
            GPIO.output(26, GPIO.LOW)
            GPIO.output(26, GPIO.LOW)
            senddata()
            print("turning on everything")
    if "off" in c:
        if "switch1" in c:
            GPIO.output(6, GPIO.HIGH)
            senddata()
        if "switch2" in c:
            GPIO.output(26, GPIO.HIGH)
            senddata()
            print("switch2 off")
        if "switch3" in c:
            GPIO.output(5, GPIO.HIGH)
            senddata()
            print("switch3 off")
        if "switch4" in c:
            print("switch4 off")
        if "switch5" in c:
            print("switch5 off")
        if "switch6" in c:
            print("switch6 off")
        if "switch7" in c:
            print("switch7 off")
        if "switch8" in c:
            print("switch8 off")
        if "everything" in c:
            print("Turning off everything")