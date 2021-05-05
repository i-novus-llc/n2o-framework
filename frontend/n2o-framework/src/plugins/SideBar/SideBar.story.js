import React from 'react'
import { setDisplayName } from 'recompose'
import { storiesOf } from '@storybook/react'

import AuthButtonContainer from '../../core/auth/AuthLogin'
import Template from '../OLD_SidebarFixTemplate'
import Wireframe from '../../components/snippets/Wireframe/Wireframe'

import SidebarContainer from './SidebarContainer'
import SideBar, { SideBar as SidebarComponent } from './SideBar'
import sidebarMetadata from './sidebarMetadata.meta.json'

const stories = storiesOf('UI Компоненты/Меню слева', module)

const NamedSidebar = setDisplayName('Sidebar')(SideBar)

stories.addParameters({
    info: {
        propTables: [SidebarComponent],
        propTablesExclude: [SideBar, Wireframe, AuthButtonContainer],
    },
})

stories
    .add(
        'Компонент',
        () => (
            <Template>
                <SideBar {...sidebarMetadata} />
                <div
                    style={{
                        width: '100%',
                        position: 'relative',
                    }}
                >
                    <Wireframe
                        style={{ top: 0, bottom: 0 }}
                        className="n2o"
                        title="Тело страницы"
                    />
                </div>
            </Template>
        ),
        {
            info: {
                text: `
      Компонент 'Боковое меню'
      ~~~js
      import Sidebar from 'n2o-framework/lib/plugins/Sidebar/Sidebar';
      
      <Sidebar
          brand="N2O Framework"
          brandImage="https://dab1nmslvvntp.cloudfront.net/wp-content/uploads/2018/04/1525068825bootstrap-logo-png-logo-228.png"
          className=""
          visible={true}
          userBox={{
              "image": "http://tiolly.by/img/empty_user.png",
              "title": "Александр Петров",
              "subTitle": "17.01.1987 * Москва",
              "items": [
                {
                  "id": "profile",
                  "label": "Профиль",
                  "iconClass": "fa fa-info",
                  "href": "/profile",
                  "type": "link"
                },
                {
                  "id": "settings",
                  "label": "Настройки",
                  "iconClass": "fa fa-cog",
                  "type": "dropdown",
                  "subItems": [
                    {
                      "id": "change-name",
                      "label": "Изменить имя",
                      "type": "link",
                      "href": "/change-name"
                    },
                    {
                      "id": "change-password",
                      "label": "Изменить пароль",
                      "type": "link",
                      "href": "/change-password"
                    }
                  ]
                }
              ]
          }}
          extra={
          [
            {
              "id": "exit",
              "label": "Выход",
              "iconClass": "fa fa-sign-out",
              "href": "/exit",
              "type": "link"
            }
          ]
        }
        items={
          [
            {
              "id": "link",
              "label": "О компании",
              "iconClass": "fa fa-info-circle",
              "href": "/",
              "type": "link"
            },
            {
              "id": "link",
              "label": "Новости",
              "iconClass": "fa fa-newspaper-o",
              "href": "/test",
              "type": "link",
              "security": {
                "roles": ["admin"]
              }
            },
            {
              "id": "link",
              "label": "Контакты",
              "href": "/test1",
              "type": "link"
            },
            {
              "id": "dropdown",
              "label": "Наши проекты",
              "iconClass": "fa fa-github",
              "type": "dropdown",
              "subItems": [
                {
                  "id": "link1",
                  "label": "N2O",
                  "type": "link",
                  "href": "/test3"
                },
                {
                  "id": "link2",
                  "label": "LSD",
                  "type": "link",
                  "href": "/test4"
                }
              ]
            }
          ]
        }
       />
      ~~~
      `,
            },
        },
    )
    .add('Ограничение доступа', () => (
        <Template>
            <SidebarContainer {...sidebarMetadata} />
            <div style={{ width: '100%', position: 'relative' }}>
                <small>
            Введите
                    {' '}
                    <mark>admin</mark>
, чтобы увидеть скрытый элемент меню
                </small>
                <AuthButtonContainer />
                <br />
            </div>
        </Template>
    ))
