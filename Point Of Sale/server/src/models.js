var mongoose = require('mongoose');
let Schema = mongoose.Schema;

let itemSchema = new Schema({
  id: Schema.ObjectId,
  name: {
    type: String,
    unique: true
  },
  price: Number,
  description: String,
  qty: Number,
  isCategory: Boolean,
  parentName: String
});

exports.Item = mongoose.model("item", itemSchema);

let supplierSchema = new Schema({
  id: Schema.ObjectId,
  name: {
    type: String,
    unique: true
  },
  company: String,
  email: String,
  mobile: String,
  website: String
});

exports.Supplier = mongoose.model("supplier", supplierSchema);

let orderScehma = new Schema({
  id: Schema.ObjectId,
  item: String,
  supplier: String,
  date_entered: {
    type: Date,
    default: Date()
  },
  price: Number,
  qty: Number,
  status: Boolean
});

exports.Order = mongoose.model("order", orderScehma);


let purchaseSchema = new Schema({
  id: String,
  cust_name: String,
  cust_email: String,
  payment_type: String,
  item: String,
  date_entered: {
    type: Date,
    default: Date()
  },
  price: Number,
  qty: Number
});

exports.Purchase = mongoose.model("purchase", purchaseSchema);
