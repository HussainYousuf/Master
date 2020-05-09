

<template>
  <v-container>

    <v-data-table :headers="headers"  :items="orders" class="elevation-1">
      <template v-slot:items="props">
        <td class="text-xs-centre">{{ props.item.cust_name }}</td>
        <td class="text-xs-centre">{{ props.item.cust_email }}</td>
        <td class="text-xs-centre">{{ props.item.payment_type }}</td>

        <td class="text-xs-centre">{{ props.item.item }}</td>

        <td class="text-xs-centre">{{ props.item.date_entered.substring(0,10) }}</td>
        <td class="text-xs-centre">{{ props.item.price }}</td>
        <td class="text-xs-centre">{{ props.item.qty }}</td>

      </template>
       


    </v-data-table>
  </v-container>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {

      headers: [
        { text: "cust_name" },
        { text: "cust_email" },
        { text: "payment_type" },
        { text: "item" },
        { text: "date_entered" },
        { text: "price" },
        { text: "qty" }
      ],
      orders: [],
    };
  },
  mounted() {
    axios.get(this.server + "purchase").then(res => {
      for (let index = 0; index < res.data.length; index++) {
        this.orders.push(res.data[index]);
      }
    });
  },
  methods: {

  }
};
</script>