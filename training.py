from tensorflow import keras
import numpy as np
from tensorflow.keras import Sequential, layers, utils

num_classes = 10

(x_training_dataset, y_training_dataset), (x_testing_dataset, y_testing_dataset) = keras.datasets.mnist.load_data()

x_training_dataset = x_training_dataset.astype("float32") / 255
x_testing_dataset = x_testing_dataset.astype("float32") / 255
x_training_dataset = np.expand_dims(x_training_dataset, -1)
x_testing_dataset = np.expand_dims(x_testing_dataset, -1)

y_training_dataset = utils.to_categorical(y_training_dataset, num_classes)
y_testing_dataset = utils.to_categorical(y_testing_dataset, num_classes)

trained_model = Sequential(
    [
        keras.Input(shape = (28, 28, 1)),
        layers.Conv2D(32, kernel_size=(3, 3), activation="relu"),
        layers.MaxPooling2D(pool_size=(2, 2)),
        layers.Conv2D(64, kernel_size=(3, 3), activation="relu"),
        layers.MaxPooling2D(pool_size=(2, 2)),
        layers.Flatten(),
        layers.Dropout(0.5),
        layers.Dense(num_classes, activation="softmax"),
    ]
)

trained_model.compile(loss="categorical_crossentropy", optimizer="adam", metrics=["accuracy"])

trained_model.fit(x_training_dataset, y_training_dataset, batch_size = 128, epochs= 15, validation_split=0.1)

score = trained_model.evaluate(x_testing_dataset, y_testing_dataset, verbose=0)
print("Test loss:", score[0])
print("Test accuracy:", score[1])

trained_model.save('saved_model/my_model')