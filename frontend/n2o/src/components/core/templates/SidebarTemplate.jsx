import React from 'react';
import Footer from '../../../plugins/Footer/Footer';
import SideBar from '../../../plugins/SideBar/SideBar';

/**
 * Class representing an Application container with {@link SideBar}
 */
class AppWithSideBar extends React.Component {
  /**
   * render
   * @returns {ReactElement} markup
   */
  render() {
    return (
      <div className="application">
        <div className="body-container">
          <SideBar
            brand={
              <span>
                N2O React <sup style={{ color: 'red' }}>beta</sup>
              </span>
            }
            items={[
              { id: 's1', label: 'Протитип', href: '/ProtoPage', type: 'link' },
              {
                id: 's2',
                label: '/ProtoPage/patients/123',
                href: '/ProtoPage/patients/123',
                type: 'link'
              },
              {
                id: 's3',
                label: '/ProtoPage/patients/123/update',
                href: '/ProtoPage/patients/123/update',
                type: 'link'
              },
              {
                id: 's4',
                label: '/ProtoPage/patients/123?name=Test&sorting.surname=ASC',
                href: '/ProtoPage/patients/123?name=Test&sorting.surname=ASC',
                type: 'link'
              }
            ]}
          />
          <div className="application-body container-fluid" {...this.props} />
        </div>
        <Footer />
      </div>
    );
  }
}

export default AppWithSideBar;
