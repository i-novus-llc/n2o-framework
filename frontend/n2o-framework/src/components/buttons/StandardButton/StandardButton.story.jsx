import React from 'react'
import { storiesOf } from '@storybook/react'
import { omit } from 'lodash'
import { Route, Switch } from 'react-router-dom'

import StandardButton from './StandardButton'

const stories = storiesOf('Кнопки', module)

const linkMeta = {
    hint: 'Высплывающая подсказка',
    visible: true,
    label: 'Внешняя ссылка',
    icon: 'fa fa-external-link',
    color: 'primary',
    url: '/my/path',
    target: '_blank',
}

const innerLinkMeta = {
    ...omit(linkMeta, ['target']),
    inner: true,
    color: 'secondary',
    icon: 'fa fa-link',
    label: 'Внутренняя ссылка',
}

const performMeta = {
    hint: 'Всплывающая подсказка',
    confirm: {
        cancelLabel: 'Нет',
        okLabel: 'Да',
        text: '`\'Выполнить действие над \'+name+\'?\'`',
        title: 'Предупреждение',
    },
    visible: true,
    disabled: false,
    count: '9',
    label: 'Выполнить',
    icon: 'fa fa-bolt',
    size: 'md',
    color: 'primary',
    action: {
        type: 'n2o/button/Dummy',
    },
}

stories
    .add('Ссылка', () => (
        <div>
            <div className="d-flex align-items-center">
                <StandardButton {...linkMeta} />
                <div className="mr-4">
                    <StandardButton {...innerLinkMeta} />
                </div>
                <Switch>
                    <Route
                        path="*"
                        render={props => (
                            <>
                                <div style={{ textAlign: 'right' }}>
                  URL:
                                    {' '}
                                    <kbd>
                                        {props.location.pathname}
                                        {props.location.search}
                                    </kbd>
                                </div>
                            </>
                        )}
                    />
                </Switch>
            </div>
        </div>
    ))
    .add('Выполнение redux action', () => (
        <div className="col-2">
            <StandardButton {...performMeta} size="sm" />
            <StandardButton {...performMeta} size="md" />
            <StandardButton {...performMeta} size="lg" />
        </div>
    ))
