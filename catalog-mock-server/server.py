import json
import sys

from flask import Flask

cli = sys.modules['flask.cli']
cli.show_server_banner = lambda *x: None

app = Flask(__name__)

with open('offers.json', 'r') as file:
    offers_data = file.read()

offers = json.loads(offers_data)

with open('products.json', 'r') as file:
    products_data = file.read()

products = json.loads(products_data)


@app.route('/api/product/', methods=['GET'])
def api_products():
    return products, 200

@app.route("/api/product/<uuid>", methods=['GET'])
def api_product(uuid):
    if uuid in products: 
        return products[uuid], 200
    else:
        return {'message': "Product not found."}, 404

@app.route('/api/offer/', methods=['GET'])
def api_offers():
    return offers, 200

@app.route("/api/offer/<uuid>", methods=['GET'])
def api_offer(uuid):
    if uuid in offers: 
        return offers[uuid], 200
    else:
        return {'message': "Offer not found."}, 404


if __name__ == '__main__':
    port = sys.argv[1]
    app.run(debug=False, port=port, host="0.0.0.0")