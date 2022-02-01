"""
Output case counts per day and deme
using (csv of) out.timeseries from antigen
"""
import pandas as pd
import yaml

DATA = 'out_timeseries.csv'
COLUMN_NAMES = ['date', 'location', 'cases']

def count_demes(data):
    df = pd.DataFrame(columns=COLUMN_NAMES)
    
    # Parse parameters.yml
    with open('parameters.yml') as f:
        parameters_data = yaml.load(f, Loader=yaml.FullLoader)
    
    # Get demeNames from parameters.yml
    deme_names = parameters_data.get('demeNames')
    
    # Iterate over each deme
    for deme in deme_names:
        deme_index = deme_names.index(deme)
        # Iterate over each row in out.timeseries
        for index, row in data.iterrows():
            # Make dataframe for current deme in current row
            next = [[row[0], deme, row[20 + 10 * deme_index]]]
            df_next = pd.DataFrame(next, columns=COLUMN_NAMES)
            df = pd.concat([df, df_next], ignore_index=True)
    
    # Output file
    df.to_csv('demes.timeseries', index=False, sep='\t')


def main():
    data = pd.read_csv(DATA)
    count_demes(data)

if __name__ == '__main__':
    main()