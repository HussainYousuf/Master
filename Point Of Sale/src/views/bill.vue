
    
    <style>
.invoice-box {
  max-width: 800px;
  margin: auto;
  padding: 30px;
  border: 1px solid #eee;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.15);
  font-size: 16px;
  line-height: 24px;
  font-family: "Helvetica Neue", "Helvetica", Helvetica, Arial, sans-serif;
  color: #555;
}

.invoice-box table {
  width: 100%;
  line-height: inherit;
  text-align: left;
}

.invoice-box table td {
  padding: 5px;
  vertical-align: top;
}

.invoice-box table tr td:nth-child(2) {
  text-align: right;
}

.invoice-box table tr.top table td {
  padding-bottom: 20px;
}

.invoice-box table tr.top table td.title {
  font-size: 45px;
  line-height: 45px;
  color: #333;
}

.invoice-box table tr.information table td {
  padding-bottom: 40px;
}

.invoice-box table tr.heading td {
  background: #eee;
  border-bottom: 1px solid #ddd;
  font-weight: bold;
}

.invoice-box table tr.details td {
  padding-bottom: 20px;
}

.invoice-box table tr.item td {
  border-bottom: 1px solid #eee;
}

.invoice-box table tr.item.last td {
  border-bottom: none;
}

.invoice-box table tr.total td:nth-child(2) {
  border-top: 2px solid #eee;
  font-weight: bold;
}

@media only screen and (max-width: 600px) {
  .invoice-box table tr.top table td {
    width: 100%;
    display: block;
    text-align: center;
  }

  .invoice-box table tr.information table td {
    width: 100%;
    display: block;
    text-align: center;
  }
}

/** RTL **/
.rtl {
  direction: rtl;
  font-family: Tahoma, "Helvetica Neue", "Helvetica", Helvetica, Arial,
    sans-serif;
}

.rtl table {
  text-align: right;
}

.rtl table tr td:nth-child(2) {
  text-align: left;
}
</style>
<template>
  <div class="invoice-box">
    <table cellpadding="0" cellspacing="0">
      <tr class="top">
        <td colspan="2">
          <table>
            <tr>
              <td class="title">Checkout Bill</td>

              <td>
                Invoice #: {{ $route.params.id }}
                <br>{{Date()}}
              </td>
            </tr>
          </table>
        </td>
      </tr>

      <tr class="information">
        <td colspan="2">
          <table>
            <tr>
              <td>
                5cube, Inc.<br>
                abc street <br>
                Karachi

              </td>

              <td>
                <br>{{cust_name}}
                <br>{{cust_email}}
              </td>
            </tr>
          </table>
        </td>
      </tr>

      <tr class="heading">
        <td>Payment Method</td>
      </tr>

      <tr class="details">
        <td>{{payment_method}}</td>
      </tr>

      <tr class="heading">
        <td>Item</td>
        <td>Qty</td>
        <td>Price</td>
      </tr>


      <tr class="item" v-for="item in items">
        <td>{{item.item}}</td>
        <td>{{item.qty}}</td>
        <td>{{item.price}}</td>
      </tr>


      <tr class="total">
        <td></td>

        <td>Total: {{total}}</td>
      </tr>
    </table>
    <v-btn @click="pdf()">print</v-btn>
    <v-btn>email</v-btn>
  </div>
</template>


<script>
import axios from "axios";
export default {
  data(){
    return{
      cust_name: "",
      cust_email: "",
      payment_method: "",
      total: 0,
      items: []

    }
  },
  mounted(){
    this.items = [];
    axios.get(this.server + "bill/"+this.$route.params.id).then(res=>{
      let items = res.data;
      this.cust_name = items[0].cust_name
      this.cust_email = items[0].cust_email
      this.payment_method = items[0].payment_type
      console.log(this.payment_method);
      for (let index = 0; index < items.length; index++) {
        this.items.push({item:items[index].item, qty:items[index].qty, price:items[index].price});
        this.total += items[index].price
      }
    });
  },
  methods: {
    pdf() {
      window.print()
    }
  },
  computed: {

  }
};
</script>
