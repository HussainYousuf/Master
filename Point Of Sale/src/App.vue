<template>
  <v-app>
    
    <v-toolbar app dark>
      <v-toolbar-side-icon @click="toogleNavBar = !toogleNavBar"></v-toolbar-side-icon>
      <v-toolbar-title class="white--text">Application</v-toolbar-title>
    </v-toolbar>

    <v-navigation-drawer v-model="toogleNavBar" app dark stateless>
      <v-list>

        <v-list-tile>
          <v-btn block flat @click="toogleNavBar = !toogleNavBar" to="/">Home</v-btn>
        </v-list-tile>

        <v-list-tile>
          <v-btn block flat @click="toogleNavBar = !toogleNavBar" to="/inventory">Inventory</v-btn>
        </v-list-tile>
        
        <v-list-tile>
          <v-btn  block flat @click="toogleNavBar = !toogleNavBar" to="/supplier">Suppliers</v-btn>
        </v-list-tile>

        <v-list-tile>
          <v-btn  block flat @click="toogleNavBar = !toogleNavBar" to="/view_orders">Orders</v-btn>
        </v-list-tile>

        <v-list-tile>
          <v-btn  block flat @click="toogleNavBar = !toogleNavBar" to="/purchases">Purchases</v-btn>
        </v-list-tile>

        


      </v-list>
    </v-navigation-drawer>

    <v-content>
      <router-view></router-view>
    </v-content>
  </v-app>
</template>


  


<script>
export default {
  data: () => ({
    toogleNavBar: false,
  }),
  
};
</script>


