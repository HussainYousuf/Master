
<template>

  <v-container >
    <!-- <v-snackbar v-model="duplicate">Name already exists</v-snackbar> -->

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
                <a style="display:inline-block; width:100%; color: #000; "  @click="getChildren(item)">{{ item.name }}</a>
            </template>
            </v-treeview>
          </v-flex>


          <v-flex xs12 md5 text-xs-center v-if="info.name">
            

            <v-text-field label="Name" v-model="info.name"></v-text-field>
            <v-textarea label="Description" v-model="info.description"></v-textarea>
            <v-text-field label="Qty" v-model="info.qty"></v-text-field>
            <v-text-field label="Price" v-model="info.price"></v-text-field>
            <v-btn @click="edit">Edit</v-btn>
            <v-btn @click="reset">reset</v-btn>
            <v-btn @click="getSupplier">order</v-btn>



          
          </v-flex>
        </v-layout>

      </v-card-text>



      <v-spacer></v-spacer>
      <v-btn block outline @click="deleteItem">Delete selected Item</v-btn>
      <v-btn block outline @click="dialog = true">Add Item</v-btn>









    <v-layout row v-if="dialog2">
      <v-dialog v-model="dialog2" persistent max-width="600px">
        <v-card>
          <v-card-title>
            <span class="headline">Add Order Details </span>
          </v-card-title>
          <v-card-text>
            <v-container grid-list-md>
              <v-layout wrap>
                  <v-flex xs12>
                    <v-autocomplete
                      v-model="supplier"
                      :items="suppliers"
                      label="Choose supplier"
                    ></v-autocomplete>
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
            <v-btn color="blue darken-1" flat @click="dialog2 = false">Close</v-btn>
            <v-btn color="blue darken-1" flat @click="order(info.name)">Save</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-layout>








   
    <v-layout row v-if="dialog">
      <v-dialog v-model="dialog" persistent max-width="600px">
        <v-card>
          <v-card-title>
            <span class="headline">Add Inventory Details </span>
          </v-card-title>
          <v-card-text>
            <v-container grid-list-md>
              <v-layout wrap>
                  <v-flex xs8>
                    <v-text-field label="Name" v-model="name"></v-text-field>
                  </v-flex>
                  <v-flex xs4>
                    <v-autocomplete
                      v-model="type"
                      :items="['Item','Category']"
                      label="Type"
                      @change="typeChanged"
                    ></v-autocomplete>
                  </v-flex>
                  <v-flex xs12>
                    <v-autocomplete
                      v-model="parentName"
                      :items="list_categories"
                      label="Parent Category"
                    ></v-autocomplete>
                  </v-flex>
                  <v-flex xs12>
                    <v-text-field label="Description" :disabled="isCategory" v-model="description"></v-text-field>
                  </v-flex>
                  <v-flex xs12>
                    <v-text-field label="Price" :disabled="isCategory" v-model="price"></v-text-field>
                  </v-flex>
              </v-layout>
            </v-container>
            <small>all fields are required</small>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" flat @click="dialog = false">Close</v-btn>
            <v-btn color="blue darken-1" flat @click="saveItem">Save</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-layout>







    </v-card>
  </v-container>
</template>


<script>
import axios from "axios";

export default {
  data: () => ({
    name: "",
    price: "",
    description: "",
    status: "",
    qty: "",
    parentName: "",

    dialog: false,
    dialog2: false,

    list_categories: ["None"],
    suppliers: [],

    supplier: "",
    order_qty: 0,
    order_price: 0.00,

    isCategory: false,
    type: "",

    selected: [],
    active: [],
    search: "",
    items: [],
    info: {
      id : "",
      name: "",
      price: "",
      description: "",
      qty: "",
    },
    temp: {
      id : "",
      name: "",
      price: "",
      description: "",
      qty: "",
    },
     
    //duplicate: false, 


  }),

  computed: {},

  mounted() {
     this.requestCategories();
  },



  watch: {},

  methods: {

    edit(){
      this.temp.id = this.info.id;
      this.temp.name = this.info.name;
      this.temp.price = Number(this.info.price) || 0;
      this.temp.description = this.info.description;
      this.temp.qty = Number(this.info.qty) || 0;
      axios.put(this.server + "item",this.info).then(res => location.reload());
    },

    reset(){
      this.info.id = this.temp.id;
      this.info.name = this.temp.name;
      this.info.description = this.temp.description;
      this.info.qty = this.temp.qty;
      this.info.price = this.temp.price;
    },

    getSupplier(){
      axios.get(this.server + "supplier").then(res=> {
        for (let index = 0; index < res.data.length; index++) {
          this.suppliers.push(res.data[index].name)
        }
      });
      this.dialog2 = true;
    },

    order(itemName){
      axios.post(this.server + "order",{
        item: itemName,
        supplier: this.supplier,
        qty: Number(this.order_qty) || 0,
        price: Number(this.order_price) || 0,
      }).then(res => (this.dialog2 = false));
      
    },

    deleteItem(item) {
      axios.post(this.server + "item/delete",{
        selected: this.selected
      }).then(res => {
        location.reload();        
      });
    },

    getChildren(item){
      if(!item.isCategory) {
        this.temp.id = this.info.id = item.id;
        this.temp.name = this.info.name = item.name;
        this.temp.price = this.info.price = item.price;
        this.temp.description = this.info.description = item.description;
        this.temp.qty = this.info.qty = item.qty;
      }else {
        axios.get(this.server + "item/"+item.name).then(res => {
          if(res.data) {
            this.$set(item,"children",res.data);
          }
        });
      }  
    },
    typeChanged() {
      if (this.type == "Category") this.isCategory = true;
      else this.isCategory = false;
    },
    requestCategories(){
      axios.get(this.server + "category").then(res=>{
        for (let index = 0; index < res.data.length; index++) {
          this.list_categories.push(res.data[index].name);
          if(res.data[index].parentName === "None") {
            this.items.push(res.data[index]);
          }
        }
      })
    },

    saveItem() {
      axios
        .post(this.server + "item", {
          name: this.name.trim(),
          price: Number(this.price) || 0,
          description: this.description || null,
          qty: Number(this.qty) || 0,
          isCategory: this.isCategory,
          parentName: this.parentName,
        })
        .then(response => {
          if (this.isCategory) {
            this.list_categories.push(this.name.trim());
            this.list_categories.sort();
          }
          if (response.data.parentName === "None") this.items.push(response.data);
          this.dialog = false;
        })
        .catch(function(error) {
          console.log(error.message);
        });
    }
  }
};
</script>
