import io
import sys
import json
from flask import Flask, jsonify, request
from snips_nlu import SnipsNLUEngine, load_resources
from snips_nlu.default_configs import CONFIG_EN


app = Flask(__name__)

def runEngine(query):
    with io.open("dataset.json") as f:
        dataset = json.load(f)

    load_resources("en")

    #with io.open("config_en.json") as f:
    #    config = json.load(f)

    #engine = SnipsNLUEngine(config=config)
    engine = SnipsNLUEngine(config=CONFIG_EN)

    engine.fit(dataset)

    parsing = engine.parse(query)
    return json.dumps(parsing, indent=2)

@app.route('/intents', methods=['POST'])
def get_answer():
    query = request.form.get('query')
    print('Input recieved from user : ', query)

    answer = runEngine(query)
    print('Answer : \n', answer)
    return answer

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
