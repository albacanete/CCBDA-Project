import pandas as pd
import numpy as np
import joblib
import os


def obj_to_df(obj):
    """
    Convert a pandas object to a dataframe
    """
    return fill_years(pd.DataFrame(obj.values()).sort_values(by=['year']))


def fill_years(df):
    """
    Fill missing years with 0
    """
    begin = min(df.year)
    end = max(df.year)
    df_new = pd.DataFrame.from_dict(dict(year=list(range(begin, end))))
    df = pd.merge(df_new, df, on='year', how='left')
    return df.fillna(-1)


class PlayerPredictor:
    def add_lag_columns(
        df: pd.DataFrame, columns="numeric", levels=[1]
    ) -> pd.DataFrame:
        """Adds laged variables to df"""

        result = df

        if isinstance(columns, list):
            df = df[columns]
        else:
            print("WARN: Shifting all columns")

        for level in levels:
            shifted = df.groupby(level="name_player").shift(level)
            result = result.join(shifted.rename(columns=lambda x: f"lag_{level}_{x}"))

        return result

    def __init__(self):
        self.target_cols = [
            "games_played",
            "goals",
            "assists",
            "minute_played",
            "value_player",
        ]
        self.need_drop = [f"lag_3_{var}" for var in self.target_cols]
        self.base = {var: f"lag_1_{var}" for var in self.target_cols}

        model_path = os.path.join(
            os.path.dirname(os.path.abspath(__file__)), "model.joblib"
        )

        self.clf = joblib.load(model_path)

        for level in [1, 2]:
            extra = {
                f"lag_{level}_{var}": f"lag_{level+1}_{var}" for var in self.target_cols
            }
            self.base = dict(**self.base, **extra)

    def step_row(self, row):
        result = row.drop(columns=self.need_drop, errors="ignore").rename(
            columns=self.base
        )
        result["age"] += 1
        result["year"] += 1
        return result

    def pred_player_lag(self, df):

        if not isinstance(df, pd.DataFrame):
            df = pd.DataFrame(df.values())

        print(df)

        df = df.rename(
            columns=dict(
                namePlayer="name_player",
                valuePlayer="value_player",
                games="games_played",
                minutes="minute_played",
                nameLeague="championship",
                nameTeam="squad_name",
            )
        ).drop(columns=["goalsConceded", "cleanSheets"])

        in_lag = (
            PlayerPredictor.add_lag_columns(
                df.set_index(["name_player", "year"]).sort_index(),
                columns=self.target_cols,
                levels=[1, 2],
            )
            .reset_index()
            .replace(-1, np.nan)
        )

        final = pd.DataFrame()

        pred = in_lag.iloc[-1:].reset_index()

        for i in range(1, 10):
            crafted = self.step_row(pred)
            result = pd.DataFrame(self.clf.predict(crafted), columns=self.target_cols)
            pred = pd.concat([crafted, result], axis=1)

            final = pd.concat([final, pred], axis=0)

        return final[
            self.target_cols + ["year", "age", "role", "squad_name", "championship"]
        ]
