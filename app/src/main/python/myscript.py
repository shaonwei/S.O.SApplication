import pywhatkit

def sendWhatsapp(phone_no, message):
    try:
        sendwhatmsg_instantly(phone_no, message,  15, False,  1)
        return "succes"
    except:
        return "fail"
