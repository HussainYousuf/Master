<template>
  <v-container>
    <v-layout wrap="wrap">
      <v-flex xs12>
        <h1 class="display-1 mb-1">Analytics</h1>
      </v-flex>


    <v-flex xs12>
        <v-card >
          <v-card-text>
            <v-sheet>
              <v-sparkline :value="value" height="100" padding="15" stroke-linecap="round" smooth>
                <template v-slot:label="item">${{ item.value }}</template>
              </v-sparkline>
            </v-sheet>
          </v-card-text>

          <v-card-text>
            <div class="">Sales Last 24h</div>
          </v-card-text>

          <v-divider></v-divider>

        </v-card>
      </v-flex>
      <v-divider></v-divider>
      <v-flex xs12>
        <v-layout wrap="wrap">
          <v-flex v-for="stat in stats" xs6="xs6">
            <v-card class="text-xs-center" height="100%">
              <v-card-text>
                <div class="display-1 mb-2">{{ stat.number }}</div>
                {{ stat.label }}
              </v-card-text>
            </v-card>
          </v-flex>
        </v-layout>
      </v-flex>
      
      <v-flex xs12="xs12">
        <v-card>
          <v-card-title primary-title class>
            Top Leads
            <v-spacer></v-spacer>
            <v-text-field v-model="newLeadsSearch" append-icon="search" label="Search"></v-text-field>
          </v-card-title>
          <v-card-text>
            <v-data-table :headers="newLeadsHeaders" :items="newLeads" :search="newLeadsSearch">
              <template slot="items" slot-scope="props">
                <td>{{ props.item.firstName }} {{ props.item.lastName }}</td>
                <td>{{ props.item.email }}</td>
                <td>{{ props.item.company }}</td>
              </template>
            </v-data-table>
          </v-card-text>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>


  <script>
const stats = [
  {
    number: "42",
    label: "New leads this week"
  },
  {
    number: "$8,312",
    label: "Sales this week"
  },
  {
    number: "233",
    label: "New leads this month"
  },
  {
    number: "$24,748",
    label: "Sales this month"
  }
];

const tasks = [
  {
    id: 0,
    title: "Book meeting for Thursday"
  },
  {
    id: 1,
    title: "Review new leads"
  },
  {
    id: 2,
    title: "Be awesome!"
  }
];

const newLeads = [
  {
    firstName: "Giselbert",
    lastName: "Hartness",
    email: "ghartness0@mail.ru",
    company: "Kling LLC"
  },
  {
    firstName: "Honey",
    lastName: "Allon",
    email: "hallon1@epa.gov",
    company: "Rogahn-Hermann"
  },
  {
    firstName: "Tommy",
    lastName: "Rickards",
    email: "trickards2@timesonline.co.uk",
    company: "Kreiger, Wehner and Lubowitz"
  },
  {
    firstName: "Giffy",
    lastName: "Farquharson",
    email: "gfarquharson3@goo.gl",
    company: "Heathcote-Funk"
  },
  {
    firstName: "Rahel",
    lastName: "Matches",
    email: "rmatches4@sfgate.com",
    company: "Maggio, Russel and Feeney"
  },
  {
    firstName: "Krystal",
    lastName: "Natte",
    email: "knatte5@opera.com",
    company: "Sanford-Feeney"
  },
  {
    firstName: "Ronnica",
    lastName: "Galliver",
    email: "rgalliver6@epa.gov",
    company: "Schaefer Group"
  },
  {
    firstName: "Jenny",
    lastName: "Bugge",
    email: "jbugge7@independent.co.uk",
    company: "Gutmann, Miller and Prosacco"
  },
  {
    firstName: "Tracee",
    lastName: "Viscovi",
    email: "tviscovi8@techcrunch.com",
    company: "Anderson, Kohler and Renner"
  },
  {
    firstName: "Teodor",
    lastName: "Fitzsimmons",
    email: "tfitzsimmons9@mediafire.com",
    company: "Durgan-Kovacek"
  }
];

const newLeadsHeaders = [
  {
    text: "Name",
    value: "firstName"
  },
  {
    text: "Email",
    value: "email"
  },
  {
    text: "Company",
    value: "company"
  }
];

export default {
  data: () => ({
    value: [423, 446, 675, 510, 590, 610, 760],
    mainMenu: {
      dashboard: "Dashboard",
      people: "Leads",
      business: "Companies",
      business_center: "Deals",
      file_copy: "Invoices",
      settings: "Settings"
    },
    stats,
    newLeads,
    newLeadsHeaders,
    newLeadsSearch: ""
  })
};
</script>
