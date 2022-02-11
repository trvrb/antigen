# import libraries
import pandas as pd
import matplotlib.pyplot as plt
from sklearn.cluster import KMeans
from scipy import stats
import seaborn as sns


def make_map():
    data = pd.read_csv('example/out.tips')

    data = data[["ag1", "ag2"]]
    # data = stats.zscore(data)

    kmeans = KMeans(n_clusters=11, n_init=40, max_iter=100, random_state=0).fit(data)
    labels = kmeans.labels_

    data['clusters'] = labels

    data.groupby(['clusters']).mean()

    sns.set_palette("Paired")
    sns.lmplot(x='ag1', y='ag2',
               data=data,
               fit_reg=False,
               hue="clusters",
               scatter_kws={"marker": "D",
                            "s": 7})
    plt.title('Map Clusters')
    plt.xlabel('ag1')
    plt.ylabel('ag2')
    plt.savefig('mapClustersSklearn.png', bbox_inches='tight')


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    make_map()
