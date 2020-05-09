
<template>

  <v-container >
    <v-card>
      <v-card-title>

        <v-text-field
          v-model="search"
          flat
          hide-details
          clearable
          clear-icon="mdi-close-circle-outline"
          label="Search"
        ></v-text-field>
        <v-btn color="primary" @click="openDialog">Check out</v-btn>
      </v-card-title>
      <v-card-text>
        <v-layout wrap justify-space-between>
          <v-flex xs12 md5>
            <v-treeview
              
              v-model="selected"
              :items="items"
              :search="search"
              open-on-click
              activatable
              selected-color="indigo"
              selectable
              expand-icon="mdi-chevron-down"
              on-icon="mdi-bookmark"
              off-icon="mdi-bookmark-outline"
              indeterminate-icon="mdi-bookmark-minus"
              transition
              :active.sync="active"
         
            >
            <template slot="label" slot-scope="{ item }">
                <a style="display:inline-block; width:100%; color: #000; " @click="getChildren(item)">{{ item.name }}</a>
            </template>
            </v-treeview>
          </v-flex>


          <v-flex xs12 md5 text-xs-center >

            <v-text-field label="Name" v-model="info.name"></v-text-field>
            <v-textarea label="Description" v-model="info.description"></v-textarea>
            <v-text-field label="Qty" v-model="info.qty"></v-text-field>
            <v-text-field label="Price" v-model="info.price"></v-text-field>
            <v-btn block color="info" v-if="info.qty <= qty && qty > 0 && info.qty > 0 && Number(info.price)" @click="add(Object.assign({},info))">Add to cart</v-btn>
          
          </v-flex>


          <v-flex xs12>


            <v-data-table :headers="headers" :items="table_items" >
              <template v-slot:items="props">
                <td class="text-xs-centre">{{ props.item.name }}</td>
                <td class="text-xs-centre">{{ props.item.description }}</td>
                <td class="text-xs-centre">{{ props.item.parentName }}</td>
                <td class="text-xs-centre">{{ props.item.qty }}</td>
                <td class="text-xs-centre">{{ props.item.price }}</td>
                <td>
                  <v-btn small right color="error" @click="cancel(props.item)">delete</v-btn>
                </td>
              </template>
            </v-data-table>

          </v-flex>

        </v-layout>
      </v-card-text>
    </v-card>

    <v-layout row v-if="dialog">
      <v-dialog v-model="dialog" persistent max-width="600px">
        <v-card>
          <v-card-title>
            <span class="headline">Add Details </span>
          </v-card-title>
          <v-card-text>
            <v-container grid-list-md>
              <v-layout wrap>



                  <v-flex xs12>
                    <v-text-field label="Customer Name" v-model="cust_name"></v-text-field>
                  </v-flex>

                  <v-flex xs12>
                    <v-text-field label="Customer Email" v-model="cust_email"></v-text-field>
                  </v-flex>

                  <v-flex xs12>
                    <v-autocomplete
                      v-model="type"
                      :items="['Cash','Check','Credit Card']"
                      label="Payment Method"
                    ></v-autocomplete>
                  </v-flex>

              </v-layout>
            </v-container>
            <small>all fields are required</small>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" flat @click="dialog = false">Close</v-btn>
            <v-btn color="blue darken-1" flat @click="checkout" >save</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-layout>


  </v-container>
</template>


<script>
import axios from "axios";
export default {
  data: () => ({
    dialog: false,
    cust_email: "",
    cust_name: "",
    type: "",
    headers: [
      { text: "Item name", value: "name" },
      { text: "Description", value: "description" },
      { text: "Category", value: "parentName" },
      { text: "Qty", value: "qty" },
      { text: "Price", value: "price" },
      { text: "Operations" }
    ],
    search: "",
    items: [],
    table_items: [],
    selected: [],
    active: [],
    added_items: [],
    info: {
      parentName: "",
      id : "",
      name: "",
      price: "",
      description: "",
      qty: "",
    },
    disable: false,
    qty: 0
  }),

  computed: {},
 
  mounted() {
    this.requestCategories();
  },

  watch: {},
  destroyed() {
  },
  methods: {
    cancel(item){
      let x = null;
      for (let index = 0; index < this.table_items.length; index++) {
        x = this.table_items[index];
        if (x.name == item.name && x.parentName == item.parentName && x.price == item.price && x.qty == item.qty  ) {
          this.$delete(this.table_items, index);
          break;
        }
      }
    },
    add(info){
      info.price = info.price * info.qty;
      if(!this.added_items.includes(info.id)){
        this.table_items.push(info);
        this.added_items.push(info.id);
      }
    },
    openDialog(){
      if(this.table_items.length < 1) return;
      this.dialog = true;
    },
    checkout(){
      let obj = {
        cust_name: this.cust_name,
        cust_email: this.cust_email,
        payment_type: this.type,
        items: []
      }
      for (let index = 0; index < this.table_items.length; index++) {
        obj.items.push(this.table_items[index]);
        axios.put(this.server + "item/quantity",this.table_items[index]).then(res=>{

        });
      }
      axios.post(this.server + "purchase",obj).then(res=> {
        this.$router.push("bill/"+res.data);
      });
      
    },
    requestCategories(){
      axios.get(this.server + "category").then(res=>{
        for (let index = 0; index < res.data.length; index++) {
          if(res.data[index].parentName === "None") {
            this.items.push(res.data[index]);
          }
        }
      });
    },
    getChildren(item){
      if(!item.isCategory) {
        this.info.id = item.id;
        this.info.name = item.name;
        this.info.parentName = item.parentName;
        this.info.price = item.price;
        this.info.description = item.description;
        this.info.qty = item.qty;
        // if(item.qty < 1) this.disable = true;
        // else this.disable = false;
        this.qty = item.qty;
      }else {
        axios.get(this.server + "item/"+item.name).then(res => {
          if(res.data) {
            this.$set(item,"children",res.data);
          }
        });
      }  
    },


  }
};
</script>
