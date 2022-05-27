import argparse
import os
import pandas as pd
import joblib
from sklearn.pipeline import Pipeline

from io import StringIO

from xgboost import XGBRegressor

from sklearn.compose import make_column_transformer

from sklearn.impute import SimpleImputer
from sklearn.preprocessing import StandardScaler, OneHotEncoder
from sklearn.feature_extraction.text import CountVectorizer


def pipeline() -> Pipeline:
    """Build model pipeline to transform, scale the data and run the model"""

    numeric_transformer = Pipeline(
        steps=[
            ("imputer", SimpleImputer(strategy="median")),
            ("scaler", StandardScaler()),
        ]
    )

    text_column = "name_player"
    categorical_columns = ["squad_name", "role", "championship"]

    # If a category has low frequency on training, label it as infrequent, when
    # running the model a category is not found it will be asigned as
    # infrequent or ignored.
    categorical_transformer = OneHotEncoder(
        handle_unknown="infrequent_if_exist",
        min_frequency=0.001,
    )

    column_trans = make_column_transformer(
        (categorical_transformer, categorical_columns),
        (CountVectorizer(), text_column),
        remainder=numeric_transformer,
    )

    clf = Pipeline(
        steps=[
            ("preprocessor", column_trans),
            ("classifier", XGBRegressor(objective="reg:squarederror")),
        ]
    )

    # Set the parameters found doing the grid search
    clf.set_params(classifier__eta=0.01, classifier__gamma=0, classifier__max_depth=7)

    return clf


def train_fn(X, y):
    return pipeline().fit(X, y)


def read_data(folder: str):
    players = pd.read_csv(os.path.join(folder, "players.csv"))
    squads = pd.read_csv(os.path.join(folder, "squads.csv"))
    df = players.join(
        squads.drop("championship", axis=1).set_index(["squad_name", "year"]),
        how="left",
        on=["squad_name", "year"],
    )

    X = df.drop("value_player", axis=1)
    y = df.value_player

    return X, y


# SageMaker endpoint functions:


def model_fn(model_dir):
    return joblib.load(os.path.join(model_dir, "model.joblib"))


def input_fn(request_body, request_content_type):
    if request_content_type == "text/csv":
        # Convert the request body into a pandas dataframe
        # and return the dataframe
        return pd.read_csv(StringIO(request_body))
    else:
        raise ValueError("Thie model only supports text/csv input")


def predict_fn(input_data, model):
    return model.predict(input_data)


def output_fn(prediction, content_type):
    return prediction.astype(str)


if __name__ == "__main__":
    # Create a parser object to collect the environment variables that are in the
    # default AWS Scikit-learn Docker container.
    parser = argparse.ArgumentParser()

    parser.add_argument(
        "--output-data-dir",
        type=str,
        default=os.environ.get("SM_OUTPUT_DATA_DIR") or ".",
    )
    parser.add_argument(
        "--model-dir", type=str, default=os.environ.get("SM_MODEL_DIR") or "."
    )
    parser.add_argument(
        "--train", type=str, default=os.environ.get("SM_CHANNEL_TRAIN") or "."
    )
    parser.add_argument(
        "--test", type=str, default=os.environ.get("SM_CHANNEL_TEST") or "."
    )

    args = parser.parse_args()

    # Seperate input variables and labels.
    train_X, train_Y = read_data(args.train)

    # Train the logistic regression model using the fit method
    model = train_fn(train_X, train_Y)

    # Save the model to the location specified by args.model_dir
    joblib.dump(model, os.path.join(args.model_dir, "model.joblib"))
