"""
In the terminal, run python setup.py install
to install required dependencies (for output_csv.py)
"""
from setuptools import setup, find_packages

setup(install_requires=[
      "wheel",
      "pandas",
      "pyyaml"
      ]
     )