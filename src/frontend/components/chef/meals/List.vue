<template>
   <v-card>
    <v-card-title primary-title>
      Plná penzia

      <v-spacer></v-spacer>
        Počet stravníkov: {{getFullBoardCount(this.stays)}}
      <v-spacer></v-spacer>

      <v-text-field
      v-model="search1"
      append-icon="mdi-magnify"
      label="Hľadať"
      single-line
      v-show="false"
      hide-details
    ></v-text-field>


    </v-card-title>
    <v-card-text>
      <v-data-table :headers="headers" :items="stays" :search="search1"  :custom-filter="customSearch" flat>

         <template v-slot:item.rooms="{ item }" >
           {{ getRooms(item.rooms) }}</template>
         <template v-slot:item.dateFrom="{ item }" >
           {{ formatDate(item.dateFrom) }}</template>
         <template v-slot:item.dateTo="{ item }">
           {{ formatDate(item.dateTo) }}</template>
         <template v-slot:item.stayCreator="{ item }">
           {{ getCustomerFullName(item.stayCreator) }}</template>
         <template v-slot:item.boardType="{ item }">
           {{staysBoardType(item.boardType) }}</template>

       </v-data-table>
    </v-card-text>
       <v-card-title primary-title>
         Polpenzia

         <v-spacer></v-spacer>
           Počet stravníkov: {{getHalfBoardCount(this.stays)}}
         <v-spacer></v-spacer>

         <v-text-field
         v-model="search"
         append-icon="mdi-magnify"
         label="Hľadať"
         single-line
         hide-details
         v-show="false"
       ></v-text-field>
       </v-card-title>
     <v-card-text>
       <v-data-table :headers="headers" :items="stays" :search="search2" :custom-filter="customSearch" flat>

         <template v-slot:item.rooms="{ item }" >
           {{ getRooms(item.rooms) }}</template>
         <template v-slot:item.dateFrom="{ item }" >
           {{ formatDate(item.dateFrom) }}</template>
         <template v-slot:item.dateTo="{ item }">
           {{ formatDate(item.dateTo) }}</template>
         <template v-slot:item.stayCreator="{ item }">
           {{ getCustomerFullName(item.stayCreator) }}</template>
         <template v-slot:item.boardType="{ item }">
           {{staysBoardType(item.boardType) }}</template>




       </v-data-table>
     </v-card-text>
   </v-card>
</template>

 <script>
 import { mapState } from "vuex";
 import moment from "moment";

 export default {
   data() {
     return {
       isLoading: true,
       search1: "plná penzia",
       search2: "polpenzia",
       dialogController: false,
       dialogUser: {},
       newUserDialog: false,
       headers: [
         {
           text: "Pobyt",
           align: "start",
           value: "id",
           groupable: false,
         },
         {
           text: "Izba",
           align: "start",
           value: "rooms",
         },

         {
           text: "Zákazník",
           align: "start",
           value: "stayCreator",
         },

         {
           text: "Počet stravníkov",
           align: "start",
           value: "accommodatedNumber",
         },
         {
           text: "Typ stravy",
           align: "start",
           value: "boardType",
         },
         {
           text: "Začiatok pobytu",
           align: "start",
           value: "dateFrom",
         },
         {
           text: "Koniec pobytu",
           align: "start",
           value: "dateTo",
         },
       ],
       };

   },
   computed: {
     ...mapState({
       stays: (state) => state.stays.items,
     }),
   },
   created() {
     this.getData();
   },
   methods: {
     async getData() {
       await Promise.all([await this.getAllStays()]);
       this.isLoading = false;
     },

     async getAllStays() {
       try {
         await this.$store.dispatch("stays/getAll");
       } catch (error) {
         console.error(error);
       }
     },

     async getAllServices() {
       try {
         await this.$store.dispatch("services/getAll");
       } catch (error) {
         console.error(error);
       }
     },

     formatDate(date) {
       return moment(date).subtract(1, 'month').format("DD. MM. YYYY");
     },

     staysBoardType(state) {
       switch (state) {
         case "HALFBOARD":
           return "polpenzia";
         case "FULLBOARD":
           return "plná penzia";
         default:
           return "";
       }
     },

     //Concatenating rooms ID to one string
     getRooms(rooms)
     {
       let tmp = "";

       for(let i = 0; i < rooms.length; i++)
       {
         if (i === 0)
         {
           tmp = tmp.concat(rooms[i].roomNumber.toString());
         }
         else
         {
           tmp = tmp.concat(",",rooms[i].roomNumber.toString());
         }
       }
       return tmp;
     },

     //Concatenating first and last name to one string
     getCustomerFullName(stayCreator)
     {
       return stayCreator.firstName.concat(" ",stayCreator.lastName);
     },

     customSearch (value, search, item)
     {
       if (Array.isArray(value)) {
         // Only date

       } else {

         let acceptedStaysBoardTypeStr =
           [
             'polpenzia', 'plná penzia', 'plna penzia',
           ]

         for (let i = 0; i < acceptedStaysBoardTypeStr.length; i++) {
           if (acceptedStaysBoardTypeStr[i].toLowerCase().startsWith(search.toLowerCase())){

             let ret = this.staysBoardType(item.boardType)

             if (ret !== "" &&
               ret.toLowerCase().startsWith(search.toLowerCase())){
               console.log(ret)
               console.log(search)
               console.log(item)
               return true;
             }
           }
         }
       }
       return false;
     },

     //Returns amount of people with FULLBOARD type of meals
     getFullBoardCount(stays)
     {
       let counter = 0;
       for (let i = 0;i < stays.length; i++)
       {
         if (stays[i].boardType === "FULLBOARD")
          {
            counter = counter + stays[i].accommodatedNumber;
          }
       }
       return counter;
     },

     //Returns amount of people with FULLBOARD type of meals
     getHalfBoardCount(stays)
     {
       let counter = 0;
       for (let i = 0;i < stays.length; i++)
       {
         if (stays[i].boardType === "HALFBOARD")
         {
           counter = counter + stays[i].accommodatedNumber;
         }
       }
       return counter;
     },
   },
 };
 </script>
