/* eslint-disable */
import React from 'react'
import PropTypes from 'prop-types'
import { withContext } from 'recompose'
import { storiesOf } from '@storybook/react'
import { text } from '@storybook/addon-knobs'

import MenuContainer from '../../Menu/MenuContainer'
import Wireframe from '../../../components/snippets/Wireframe/Wireframe'
import AuthButtonContainer from '../../../core/auth/AuthLogin'

import SimpleHeader from './SimpleHeader'
import simpleHeaderMetadata from './simpleHeaderData.json'

const stories = storiesOf('UI Компоненты/Меню сверху', module)

const MenuContext = withContext(
    {
        getMenu: PropTypes.func,
    },
    () => ({
        getMenu: () => simpleHeaderMetadata,
    }),
)(MenuContainer)

stories
    .add('Компонент', () => {
        const props = {
            brandImage: text(
                'brandImage',
                'https://avatars0.githubusercontent.com/u/25926683?s=200&v=4',
            ),
            activeId: 'link',
            color: 'inverse',
            fixed: false,
            collapsed: false,
            className: 'n2o',
            search: false,
            items: simpleHeaderMetadata.items,
            extraItems: simpleHeaderMetadata.extraItems,
        }

        return (
            <div>
                <SimpleHeader {...props} />
                <div style={{ padding: '200px', position: 'relative' }}>
                    <Wireframe className="n2o" title="Тело страницы" />
                </div>
            </div>
        )
    })
    .add('Метаданные', () => (
        <div>
            <MenuContext render={config => <SimpleHeader {...config} />} />
            <div style={{ padding: '200px', position: 'relative' }}>
                <Wireframe className="n2o" title="Тело страницы" />
            </div>
        </div>
    ))
    .add('Ограничение доступа', () => (
        <div>
            <MenuContext render={config => <SimpleHeader {...config} />} />
            <div style={{ padding: '200px', position: 'relative' }}>
                <small>
                    Введите
                    {' '}
                    <mark>admin</mark>
                    , чтобы увидеть скрытый элемент меню
                </small>
                <AuthButtonContainer />
                <br />
            </div>
        </div>
    ))
