import React from "react";
import Footer from "n2o-framework/lib/plugins/Footer/Footer";
import MenuContainer from "n2o-framework/lib/plugins/Menu/MenuContainer";

import NavbarWithTime from "./plugins/NavbarWithTime/NavbarWithTime";

export default function AppTemplate({ children }) {
  return (
    <div className="application">
      <MenuContainer render={config => <NavbarWithTime {...config} />} />
      <div className="application-body container-fluid">{children}</div>
    </div>
  );
}
