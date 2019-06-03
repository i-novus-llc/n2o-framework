import React from "react";
import NavbarWithTime from "../NavbarWithTime";

export default [
  {
    component: NavbarWithTime,
    name: "Navbar с часами",
    props: {
      brand: "Название",
      timeFormat: "HH:mm:ss",
      items: [
        {
          href: "#",
          id: "one",
          label: "Один",
          linkType: "outer",
          type: "link"
        },
        {
          href: "#",
          id: "two",
          label: "Два",
          linkType: "outer",
          type: "link"
        },
        {
          href: "#",
          id: "three",
          label: "Три",
          linkType: "outer",
          type: "link"
        }
      ]
    },
    reduxState: {}
  }
];
