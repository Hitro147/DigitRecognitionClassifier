import flask
from flask import request
import os
import tensorflow as tf
from werkzeug.utils import secure_filename
from PIL import Image
import numpy as np
import shutil

model = None

def load_model():
    global model
    # model variable refers to the global variable
    model = tf.keras.models.load_model('saved_model/my_model')

def test_predictor(tmpname):
    # reading image
    img = Image.open(tmpname)
    # converting image to greyscale
    greyscale_img = img.convert("L")
    # resizing image to 28x28
    resized_greyscale_img = greyscale_img.resize((28,28))

    # converting Image object to numpy array
    img_array = np.asarray(resized_greyscale_img).astype(np.float32)
    
    # restructing from 28x28 to 28x28x1
    restructured_img_array = []
    for horz_pixel_iter in img_array.tolist():
        pxl_array = []
        for pxl in horz_pixel_iter:
            pxl_array.append([pxl])
        restructured_img_array.append(pxl_array)
    
    # making it as an array of images
    input_image_data = np.asarray([restructured_img_array])
    confidence_array = model.predict(input_image_data)
    confidence_list = confidence_array.tolist()[0]
    max_confidence_val = max(confidence_list)
    category_name = str(confidence_list.index(max_confidence_val))
    return category_name

app = flask.Flask(__name__)

@app.route("/upload", methods=["POST"])
def upload_image():
    if request.method == 'POST':
        if 'file' not in request.files:
            return 'No file attached in request'
        f = request.files['file']
        if f.filename == '':
            return 'No file selected'
        
        # saving image in a temporary folder
        temp_folder = "temp"
        tmpname = temp_folder+'/'+str(secure_filename(f.filename))
        if not os.path.exists(temp_folder):
            os.makedirs(temp_folder)
        f.save(tmpname)

        category_name = test_predictor(tmpname)
        sfname = category_name+'/'+str(secure_filename(f.filename))
        if not os.path.exists(category_name):
            os.makedirs(category_name)
        shutil.move(tmpname,sfname)
        return 'Done'

if __name__ == "__main__":
    load_model()
    app.run(host="0.0.0.0",debug=True)