import React from 'react'
import classNames from 'classnames'
import { compose, setDisplayName } from 'recompose'

import withWidgetProps from '../withWidgetProps'

import { ScrollSpyRegionTypes } from './ScrollSpyRegionTypes'
import { Menu } from './Menu'
import { ContentGroup } from './ContentGroup'

/* FIXME remove it */
const mockMenu = [
    {
        id: 'element1',
        title: 'Menu elements title 1',
        content: [
            {
                src: 'FormWidget',
                id: 'table1',
                datasource: 'table1',
                form: {
                    fieldsets: [
                        {
                            src: 'StandardFieldset',
                            rows: [
                                {
                                    cols: [
                                        {
                                            fields: [
                                                {
                                                    src: 'StandardField',
                                                    id: 'test',
                                                    required: false,
                                                    visible: true,
                                                    enabled: true,
                                                    label: 'always-refresh="true", lazy="true"',
                                                    noLabelBlock: false,
                                                    dependency: [],
                                                    control: {
                                                        src: 'InputText',
                                                        id: 'test',
                                                    },
                                                },
                                            ],
                                        },
                                    ],
                                },
                            ],
                        },
                    ],
                    modelPrefix: 'resolve',
                    prompt: false,
                    autoFocus: false,
                },
            },
            {
                src: 'FormWidget',
                id: 'table12',
                datasource: 'table12',
                form: {
                    fieldsets: [
                        {
                            src: 'StandardFieldset',
                            rows: [
                                {
                                    cols: [
                                        {
                                            fields: [
                                                {
                                                    src: 'StandardField',
                                                    id: 'test',
                                                    required: false,
                                                    visible: true,
                                                    enabled: true,
                                                    label: 'always-refresh="true", lazy="true"',
                                                    noLabelBlock: false,
                                                    dependency: [],
                                                    control: {
                                                        src: 'InputText',
                                                        id: 'test',
                                                    },
                                                },
                                            ],
                                        },
                                    ],
                                },
                            ],
                        },
                    ],
                    modelPrefix: 'resolve',
                    prompt: false,
                    autoFocus: false,
                },
            },
        ],
    },
    {
        id: 'element2',
        title: 'Menu element2 with dropdown',
        menu: [{
            id: 'element3',
            title: 'dropdown element id 3',
            content: [
                {
                    src: 'FormWidget',
                    id: 'table2',
                    datasource: 'table2',
                    form: {
                        fieldsets: [
                            {
                                src: 'StandardFieldset',
                                rows: [
                                    {
                                        cols: [
                                            {
                                                fields: [
                                                    {
                                                        src: 'StandardField',
                                                        id: 'test',
                                                        required: false,
                                                        visible: true,
                                                        enabled: true,
                                                        label: 'always-refresh="true", lazy="true"',
                                                        noLabelBlock: false,
                                                        dependency: [],
                                                        control: {
                                                            src: 'InputText',
                                                            id: 'test',
                                                        },
                                                    },
                                                ],
                                            },
                                        ],
                                    },
                                ],
                            },
                        ],
                        modelPrefix: 'resolve',
                        prompt: false,
                        autoFocus: false,
                    },
                },
                {
                    src: 'FormWidget',
                    id: 'table121',
                    datasource: 'table121',
                    form: {
                        fieldsets: [
                            {
                                src: 'StandardFieldset',
                                rows: [
                                    {
                                        cols: [
                                            {
                                                fields: [
                                                    {
                                                        src: 'StandardField',
                                                        id: 'test',
                                                        required: false,
                                                        visible: true,
                                                        enabled: true,
                                                        label: 'always-refresh="true", lazy="true"',
                                                        noLabelBlock: false,
                                                        dependency: [],
                                                        control: {
                                                            src: 'InputText',
                                                            id: 'test',
                                                        },
                                                    },
                                                ],
                                            },
                                        ],
                                    },
                                ],
                            },
                        ],
                        modelPrefix: 'resolve',
                        prompt: false,
                        autoFocus: false,
                    },
                },
            ],
        },
        {
            id: 'element4',
            title: 'dropdown element id 4',
            content: [
                {
                    src: 'FormWidget',
                    id: 'table2',
                    datasource: 'table2',
                    form: {
                        fieldsets: [
                            {
                                src: 'StandardFieldset',
                                rows: [
                                    {
                                        cols: [
                                            {
                                                fields: [
                                                    {
                                                        src: 'StandardField',
                                                        id: 'test',
                                                        required: false,
                                                        visible: true,
                                                        enabled: true,
                                                        label: 'always-refresh="true", lazy="true"',
                                                        noLabelBlock: false,
                                                        dependency: [],
                                                        control: {
                                                            src: 'InputText',
                                                            id: 'test',
                                                        },
                                                    },
                                                ],
                                            },
                                        ],
                                    },
                                ],
                            },
                        ],
                        modelPrefix: 'resolve',
                        prompt: false,
                        autoFocus: false,
                    },
                },
                {
                    src: 'FormWidget',
                    id: 'table121',
                    datasource: 'table121',
                    form: {
                        fieldsets: [
                            {
                                src: 'StandardFieldset',
                                rows: [
                                    {
                                        cols: [
                                            {
                                                fields: [
                                                    {
                                                        src: 'StandardField',
                                                        id: 'test',
                                                        required: false,
                                                        visible: true,
                                                        enabled: true,
                                                        label: 'always-refresh="true", lazy="true"',
                                                        noLabelBlock: false,
                                                        dependency: [],
                                                        control: {
                                                            src: 'InputText',
                                                            id: 'test',
                                                        },
                                                    },
                                                ],
                                            },
                                        ],
                                    },
                                ],
                            },
                        ],
                        modelPrefix: 'resolve',
                        prompt: false,
                        autoFocus: false,
                    },
                },
            ],
        },
        ],
    },
]

export function ScrollSpyRegion(
    /* FIXME remove defaults values */
    {
        id = 'ScrollSpyRegion',
        placement = 'left',
        title: menuTitle = 'Menu title',
        active = 'element4',
        className,
        headlines = true,
        style,
        menu = mockMenu,
        pageId,
    },
) {
    return (
        <div
            className={classNames(
                'd-flex',
                'n2o-scroll-spy-region',
                className,
                {
                    'flex-row-reverse': placement === 'right',
                },
            )}
            id={id}
            style={style}
        >

            <Menu
                menu={menu}
                menuTitle={menuTitle}
                active={active}
            />
            <ContentGroup
                menu={menu}
                pageId={pageId}
                headlines={headlines}
            />
        </div>
    )
}

ScrollSpyRegion.propTypes = ScrollSpyRegionTypes

export default compose(
    setDisplayName('ScrollSpyRegion'),
    withWidgetProps,
)(ScrollSpyRegion)
