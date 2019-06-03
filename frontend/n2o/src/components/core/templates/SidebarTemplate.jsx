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
            userBox={{
              image: 'http://tiolly.by/img/empty_user.png',
              title: 'Александр Петров',
              subTitle: '17.01.1987 * Москва',
              items: [
                {
                  id: 'profile',
                  label: 'Профиль',
                  iconClass: 'fa fa-info',
                  href: '/profile',
                  type: 'link',
                },
                {
                  id: 'settings',
                  label: 'Настройки',
                  iconClass: 'fa fa-cog',
                  type: 'dropdown',
                  subItems: [
                    {
                      id: 'change-name',
                      label: 'Изменить имя',
                      type: 'link',
                      href: '/change-name',
                    },
                    {
                      id: 'change-password',
                      label: 'Изменить пароль',
                      type: 'link',
                      href: '/change-password',
                    },
                  ],
                },
              ],
            }}
            brand={'N2O Framework'}
            visible={true}
            brandImage={
              'https://dab1nmslvvntp.cloudfront.net/wp-content/uploads/2018/04/1525068825bootstrap-logo-png-logo-228.png'
            }
            items={[
              {
                id: 'link',
                label: 'О компании',
                iconClass: 'fa fa-info-circle',
                href: '/',
                type: 'link',
              },
              {
                id: 'link',
                label: 'Новости',
                iconClass: 'fa fa-newspaper-o',
                href: '/test',
                type: 'link',
                security: {
                  roles: ['admin'],
                },
              },
              {
                id: 'link',
                label: 'Контакты',
                href: '/test1',
                type: 'link',
              },
              {
                id: 'dropdown',
                label: 'Наши проекты',
                iconClass: 'fa fa-github',
                type: 'dropdown',
                subItems: [
                  {
                    id: 'link1',
                    label: 'N2O',
                    type: 'link',
                    href: '/test3',
                  },
                  {
                    id: 'link2',
                    label: 'LSD',
                    type: 'link',
                    href: '/test4',
                  },
                ],
              },
            ]}
            extra={[
              {
                id: 'exit',
                label: 'Выход',
                iconClass: 'fa fa-sign-out',
                href: '/exit',
                type: 'link',
              },
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
