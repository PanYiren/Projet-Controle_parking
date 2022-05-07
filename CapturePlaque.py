import cv2
import pytesseract
from pytesseract import Output
from datetime import date
from datetime import datetime
import time

cap = cv2.VideoCapture(0)
cap.set(cv2.CAP_PROP_BUFFERSIZE, 1)
 
while True:
    # Capture frame-by-frame
    ret, frame = cap.read()
 
    d = pytesseract.image_to_data(frame, output_type=Output.DICT)
    n_boxes = len(d['text'])
    for i in range(n_boxes):
        if int(d['conf'][i]) > 60:
            (text, x, y, w, h) = (d['text'][i], d['left'][i], d['top'][i], d['width'][i], d['height'][i])
            # don't show empty text
            if text and text.strip() != "":
              if len(text) == 9:
                frame = cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)
                frame = cv2.putText(frame, text, (x, y - 10), cv2.FONT_HERSHEY_SIMPLEX, 1.0, (0, 0, 255), 3)           
                cv2.imwrite('image.jpg', frame)

                
    cv2.imshow('frame', frame)



    
              # now = datetime.now()
              #  dt_string = now.strftime("%d/%m/%Y %H:%M:%S")
                 
                 
               # cv2.imwrite('image'+ dt_string  +'.jpg', frame)
            #    print("date and time =", dt_string)  
             #   time.sleep(20)
                    
                    
    # Display the resulting frame
    cv2.imshow('frame', frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break
 
# When everything done, release the capture
cap.release()
cv2.destroyAllWindows()
