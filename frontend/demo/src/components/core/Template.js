import React from 'react';
import { Collapse, Navbar, NavbarToggler, Nav, NavItem, Button } from 'reactstrap';

import SideBar from 'n2o-framework/lib/plugins/SideBar/SideBar';
import Icon from 'n2o-framework/lib/components/snippets/Icon/Icon';

class Template extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      sidebarOpen: true,
      isOpen: false
    };

    this.handleToggleSidebar = this.handleToggleSidebar.bind(this);
    this.toggle = this.toggle.bind(this);
  }

  handleToggleSidebar() {
    this.setState({
      sidebarOpen: !this.state.sidebarOpen
    })
  }

  toggle() {
    this.setState({
      isOpen: !this.state.isOpen
    });
  }

  render() {
    return (
      <div className="application">
        <div className="body-container">
          <SideBar color="inverse"
                   brand="N2O React Demo"
                   visible={this.state.sidebarOpen}
                   collapse={false}
                   search
                   items={[
                     {
                       "id": "dashboard",
                       "label": "Dashboard",
                       "iconClass": "fa fa-tachometer",
                       "href": "/Dashboard",
                       "type": "dropdown",
                       "subItems": [
                         {
                           "id": "dashboardv1",
                           "label": "Через XML",
                           "type": "link",
                           "href": "/Dashboard"
                         },
                         {
                           "id": "dashboardv2",
                           "label": "Через JSX",
                           "type": "link",
                           "href": "/Dashboard/v2"
                         }
                       ]
                     },
                     {
                       "id": "examples",
                       "label": "Примеры",
                       "iconClass": "fa fa-puzzle-piece",
                       "href": "/Containers",
                       "type": "dropdown",
                       "subItems": [
                         {
                           "id": "containers",
                           "label": "Список пациентов",
                           "type": "link",
                           "href": "/Containers"
                         },
                         {
                           "id": "protopage",
                           "label": "Протитип",
                           "type": "link",
                           "href": "/ProtoPage"
                         }
                       ]
                     }
                   ]}/>
          <div className="application-body">
            <Navbar className="demo-header" color="light" light expand="md">
              <NavbarToggler onClick={this.toggle}/>
              <Collapse isOpen={this.state.isOpen} navbar>
                <Nav navbar>
                  <NavItem>
                    <Button color="link" onClick={this.handleToggleSidebar}><Icon name="fa fa-bars"/></Button>
                  </NavItem>
                </Nav>
                <Nav className="ml-auto" navbar>
                  <NavItem className='ml-1'>
                    <Button><Icon name="fa fa-comments"/></Button>
                  </NavItem>
                  <NavItem className='ml-1'>
                    <Button><Icon name="fa fa-bell"/></Button>
                  </NavItem>
                  <NavItem className='ml-1'>
                    <Button><Icon name="fa fa-user"/> Админов Админ</Button>
                  </NavItem>
                </Nav>
              </Collapse>
            </Navbar>
            <div className="content" {...this.props}/>
          </div>
        </div>
      </div>
    );
  }

}

export default Template;
