

<template>
  <v-container>
    <v-data-table :headers="headers" :items="orders" class="elevation-1">
      <template v-slot:items="props">
        <td class="text-xs-centre">{{ props.item.item }}</td>
        <td class="text-xs-centre">{{ props.item.qty }}</td>
        <td class="text-xs-centre">{{ props.item.supplier }}</td>
        <td class="text-xs-centre">{{ props.item.date_entered.substring(0,10) }}</td>
        <td class="text-xs-centre">{{ props.item.price }}</td>
        <td>
          <v-btn
            v-if="!props.item.status"
            right
            small
            @click="changeState(props.item)"
            color="error"
          >Pending</v-btn>
          <v-btn v-else small right color="success">Completed</v-btn>
          <!-- <v-btn small right color="info" @click="edit(props.item)">Edit</v-btn> -->
          <v-btn small right color="warning" @click="cancel(props.item)">delete</v-btn>
        </td>
      </template>


        <!-- <v-layout row v-if="dialog">
          <v-dialog v-model="dialog" persistent max-width="600px">
            <v-card>
              <v-card-title>
                <span class="headline">Add Order Details</span>
              </v-card-title>
              <v-card-text>
                <v-container grid-list-md>
                  <v-layout wrap>
                    <v-flex xs12>
                      <v-autocomplete v-model="supplier" :items="suppliers" label="Choose supplier"></v-autocomplete>
                    </v-flex>

                    <v-flex xs12>
                      <v-text-field label="qty" :disabled="isCategory" v-model="order_qty"></v-text-field>
                    </v-flex>

                    <v-flex xs12>
                      <v-text-field label="Price" :disabled="isCategory" v-model="order_price"></v-text-field>
                    </v-flex>
                  </v-layout>
                </v-container>
                <small>all fields are required</small>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" flat @click="dialog = false">Close</v-btn>
                <v-btn color="blue darken-1" flat @click="order(info.name)">Save</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-layout> -->


    </v-data-table>
  </v-container>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      // editItems: {
      //   item: "",
      //   qty: "",
      //   supplier: "",
      //   date_entered: "",
      //   price: "",
      //   id: ""
      // },
      headers: [
        { text: "Item name", value: "item" },
        { text: "Qty", value: "qty" },
        { text: "Supplier name", value: "supplier" },
        { text: "Date entered", value: "date_entered" },
        { text: "Price", value: "price" },
        { text: "Operations" }
      ],
      orders: [],
     // dialog: false
    };
  },
  mounted() {
    axios.get(this.server + "order").then(res => {
      for (let index = 0; index < res.data.length; index++) {
        this.orders.push(res.data[index]);
      }
    });
  },
  methods: {
    changeState(order) {
      order.status = !order.status;
      axios.put(this.server + "order", order);
    },
    // edit(order) {
    //   editItems.item = order.item;
    //   editItems.id = order.id;
    //   editItems.supplier = order.supplier;
    //   editItems.date_entered = order.date_entered;
    //   editItems.qty = order.qty;
    //   editItems.price = order.price;
    //   axios.put("http://localhost:8081/order/", editItems);
    //   this.dialog = true;
    // },
    cancel(order) {
      for (let index = 0; index < this.orders.length; index++) {
        if ((this.orders[index].id = order.id)) {
          this.$delete(this.orders, index);
          axios.post(this.server + "order/delete", order);
          break;
        }
      }
    }
  }
};
</script>