
                
<template>
  <v-container>
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

      <v-layout justify-space-between wrap>
        <v-flex xs12 md5>
           <v-treeview
              
              :items="suppliers"
              :search="search"
              open-on-click
              activatable
              selected-color="indigo"
              selectable
              on-icon="mdi-account"
              off-icon="mdi-account"
              indeterminate-icon="mdi-account"
              transition
              :active.sync="active"
         
            >
            <template slot="label" slot-scope="{ item }">
                <a style="display:inline-block; width:100%; color: #000;" @click="getDetails(item)">{{ item.name }}</a>
            </template>
            </v-treeview>
        </v-flex>


        
        <v-flex xs12 md5 text-xs-center>
        
            <v-text-field label="Name" v-model="info.name"></v-text-field>
            <v-text-field label="Company" v-model="info.company"></v-text-field>
            <v-text-field label="Email" v-model="info.email"></v-text-field>
            <v-text-field label="Mobile" v-model="info.mobile"></v-text-field>
            <v-text-field label="WebSite" v-model="info.website"></v-text-field>
            <v-btn  @click="edit">Edit</v-btn>
            <v-btn  @click="reset">reset</v-btn>
            <v-btn  @click="deleteSupplier">delete</v-btn>
            <!-- <v-btn to="/supplier_details" >View ledger</v-btn> -->
            
        </v-flex>
      </v-layout>

    <v-btn block outline @click="dialog = true">Add Supplier</v-btn>

    </v-card>

    <v-layout row v-if="dialog">
      <v-dialog v-model="dialog" persistent max-width="600px">
        <v-card>
          <v-card-title>
            <span class="headline">Add Supplier Details </span>
          </v-card-title>
          <v-card-text>
            <v-container grid-list-md>
              <v-layout wrap>
                <v-flex xs12 sm6>
                  <v-text-field label="Name" v-model="name" required></v-text-field>
                </v-flex>
                <v-flex xs12 sm6>
                  <v-text-field label="Company" v-model="company"></v-text-field>
                </v-flex>
                <v-flex xs12>
                  <v-text-field label="Email" v-model="email" required></v-text-field>
                </v-flex>
                <v-flex xs12 sm6>
                  <v-text-field label="Mobile" v-model="mobile" required></v-text-field>
                </v-flex>
                <v-flex xs12 sm6>
                  <v-text-field label="Website" v-model="website"></v-text-field>
                </v-flex>
              </v-layout>
            </v-container>
            <small>all fields are required</small>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" flat @click="dialog = false">Close</v-btn>
            <v-btn color="blue darken-1" flat @click="addSupplier">Save</v-btn>
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
    search: "",
    name: "",
    company: "",
    email: "",
    mobile: "",
    website: "",
    dialog: false,
    active: [],
    suppliers: [],

    temp:{
      id: "",
      name: "",
      email: "",
      mobile: "",
      website: "",
      company: "",
    },

    info:{
      id: "",
      name: "",
      email: "",
      mobile: "",
      website: "",
      company: "",
    }
  }),
  mounted(){
    axios.get(this.server + "supplier").then(res => {
      for (let index = 0; index < res.data.length; index++) 
        this.suppliers.push(res.data[index]);
    });
  },
  methods: {
    getDetails(item){
      this.temp.id = this.info.id = item.id;
      this.temp.name = this.info.name = item.name;
      this.temp.email = this.info.email = item.email;
      this.temp.company = this.info.company = item.company;
      this.temp.website = this.info.website = item.website;
      this.temp.mobile = this.info.mobile = item.mobile;
    },
    addSupplier() {
      axios
        .post(this.server + "supplier", {
          name: this.name,
          company: this.company,
          email: this.email,
          mobile: this.mobile,
          website: this.website
        })
        .then(response => {
          location.reload();
        })
        .catch(function(error) {
          console.log(error);
        });
    },
    deleteSupplier() {
      axios.post(this.server + "supplier/delete",{id:this.info.id}).then(res => {
        location.reload();
      });
    },
    edit(){
      this.temp.name = this.info.name ;
      this.temp.email = this.info.email ;
      this.temp.company = this.info.company ;
      this.temp.website = this.info.website ;
      this.temp.mobile = this.info.mobile ;
      axios.put(this.server + "supplier",this.info).then(res=>location.reload());
    },
    reset(){
      this.info.name = this.temp.name
      this.info.email = this.temp.email ;
      this.info.company = this.temp.company ;
      this.info.website = this.temp.website ;
      this.info.mobile = this.temp.mobile;
    }
  }
};
</script>
