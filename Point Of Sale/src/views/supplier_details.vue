

<template>
  <v-container>
    <v-toolbar prominent flat color="white">
      <v-text-field v-model="search" append-icon="search" label="Search" single-line hide-details></v-text-field>
    </v-toolbar>
    <v-data-table  :headers="headers" :items="desserts">
      <template v-slot:items="props">
        <td>{{ props.item.name }}</td>
        <td class>{{ props.item.fat }}</td>
        <td class>{{ props.item.carbs }}</td>
        <td class>{{ props.item.protein }}</td>
        <td class>{{ props.item.calories }}</td>
        <td>
          <v-icon small class="mr-2" @click="editItem(props.item)">edit</v-icon>
          <v-icon small @click="deleteItem(props.item)">delete</v-icon>
        </td>
      </template>
    </v-data-table>

    <v-dialog v-model="dialog" max-width="500px">
      <template v-slot:activator="{ on }">
        <v-btn class="primary" block v-on="on">New Item</v-btn>
      </template>
      <v-card>
        <v-card-title>
          <span class="headline">New Item</span>
        </v-card-title>

        <v-card-text>
          <v-container grid-list-md>
            <v-layout wrap>
              <v-flex xs12 sm6 md4>
                <v-text-field v-model="editedItem.name" label="Name"></v-text-field>
              </v-flex>
              <v-flex xs12 sm6 md4>
                <v-text-field v-model="editedItem.calories" label="Qty"></v-text-field>
              </v-flex>
              <v-flex xs12 sm6 md4>
                <v-text-field v-model="editedItem.fat" label="Category"></v-text-field>
              </v-flex>
              <v-flex xs12 sm6 md4>
                <v-text-field v-model="editedItem.carbs" label="WholeSale"></v-text-field>
              </v-flex>
              <v-flex xs12 sm6 md4>
                <v-text-field v-model="editedItem.protein" label="Retail"></v-text-field>
              </v-flex>
            </v-layout>
          </v-container>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" flat @click="close">Cancel</v-btn>
          <v-btn color="blue darken-1" flat @click="save">Save</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>





<script>
export default {
  data: () => ({
    search: "",
    dialog: false,
    headers: [
      {
        text: "Item Name",
        align: "left",
        value: "name"
      },
      { text: "Qty", value: "fat" },
      { text: "Category", value: "carbs" },
      { text: "WholeSale", value: "protein" },
      { text: "Retail", value: "calories" },
      { text: "Actions", value: "name" }
    ],
    desserts: [],
    editedIndex: -1,
    editedItem: {
      name: "",
      calories: 0,
      fat: 0,
      carbs: 0,
      protein: 0
    },
    defaultItem: {
      name: "",
      calories: 0,
      fat: 0,
      carbs: 0,
      protein: 0
    }
  }),

  computed: {},

  watch: {
    dialog(val) {
      val || this.close();
    }
  },

  created() {
    this.initialize();
  },

  methods: {
    initialize() {
      this.desserts = [
        {
          name: "Frozen Yogurt",
          calories: 159,
          fat: 6.0,
          carbs: "desert",
          protein: 159
        },
        {
          name: "Ice cream sandwich",
          calories: 237,
          fat: 9.0,
          carbs: "desert",
          protein: 159
        },
        {
          name: "Eclair",
          calories: 262,
          fat: 16.0,
          carbs: "desert",
          protein: 159
        },
        {
          name: "Cupcake",
          calories: 305,
          fat: 3,
          carbs: "desert",
          protein: 159
        },
        {
          name: "Gingerbread",
          calories: 356,
          fat: 16.0,
          carbs: "desert",
          protein: 159
        },
        {
          name: "Jelly bean",
          calories: 375,
          fat: 0.0,
          carbs: "desert",
          protein: 159
        },
        {
          name: "Lollipop",
          calories: 392,
          fat: 2,
          carbs: "desert",
          protein: 159
        },
        {
          name: "Honeycomb",
          calories: 408,
          fat: 3,
          carbs: "desert",
          protein: 6.5
        },
        {
          name: "Donut",
          calories: 452,
          fat: 25.0,
          carbs: "desert",
          protein: 159
        },
        {
          name: "KitKat",
          calories: 518,
          fat: 26.0,
          carbs: "desert",
          protein: 7
        }
      ];
    },

    editItem(item) {
      this.editedIndex = this.desserts.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    deleteItem(item) {
      const index = this.desserts.indexOf(item);
      confirm("Are you sure you want to delete this item?") &&
        this.desserts.splice(index, 1);
    },

    close() {
      this.dialog = false;
      setTimeout(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      }, 300);
    },

    save() {
      if (this.editedIndex > -1) {
        Object.assign(this.desserts[this.editedIndex], this.editedItem);
      } else {
        this.desserts.push(this.editedItem);
      }
      this.close();
    }
  }
};
</script>


