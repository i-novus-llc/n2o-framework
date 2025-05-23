module.exports = {
    docs: [
        'introduction',
        {
            type: 'category',
            label: 'Руководство',
            items: [
                'guides/manual/getstarted',
                'guides/manual/configs',
                'guides/manual/components',
                'guides/manual/datasources',
                'guides/manual/data',
                'guides/manual/actions',
                'guides/manual/defaultvalue',
                'guides/manual/dependency',
                'guides/manual/filters',
                'guides/manual/validation',
                'guides/manual/routing',
                'guides/manual/setting',
                'guides/manual/context',
                'guides/manual/dynamic',
                'guides/manual/events',
                'guides/manual/processing',
                'guides/manual/security',
                'guides/manual/customization',
                'guides/manual/conclusion',
            ],
        },
        {
            type: 'category',
            label: 'XML API',
            items: [
                'guides/xml/application',
                'guides/xml/page',
                'guides/xml/region',
                'guides/xml/widget',
                'guides/xml/fieldset',
                'guides/xml/field',
                'guides/xml/action',
                'guides/xml/cell',
                'guides/xml/chart',
                'guides/xml/object',
                'guides/xml/query',
                'guides/xml/dataprovider',
                'guides/xml/datasource',
                'guides/xml/menu',
                'guides/xml/button',
                'guides/xml/event',
                'guides/xml/access',
                'guides/xml/accesspoint',
                'guides/xml/processing',
                'guides/xml/base',
            ],
        },
        {
            type: 'category',
            label: 'Миграция',
            items: [
                'guides/migration/schemas',
                'guides/migration/to_7_29',
                'guides/migration/to_7_28',
                'guides/migration/to_7_27',
                'guides/migration/to_7_26',
                'guides/migration/to_7_25',
                'guides/migration/to_7_24',
                'guides/migration/to_7_23',
                'guides/migration/to_7_22',
                'guides/migration/to_7_21',
                'guides/migration/to_7_20',
                'guides/migration/to_7_19',
                'guides/migration/to_7_18',
                'guides/migration/to_7_17',
                'guides/migration/to_7_16',
                'guides/migration/to_7_15',
                'guides/migration/to_7_14',
                'guides/migration/to_7_13',
                'guides/migration/to_7_12',
                'guides/migration/to_7_11',
                'guides/migration/to_7_10',
                'guides/migration/to_7_9',
                'guides/migration/to_7_8',
                'guides/migration/to_7_7',
                'guides/migration/to_7_6',
                'guides/migration/to_7_5',
                'guides/migration/to_7_4',
                'guides/migration/to_7_2',
                'guides/migration/to_7_1',
                'guides/migration/to_7_0',
            ]
        }
    ],
    examples: [
        {
            type: 'category',
            label: 'Компоненты',
            items: [
                {
                    type: 'category',
                    label: 'Общие',
                    items: [
                        'buttons',
                    ],
                },
                {
                    type: 'category',
                    label: 'Разметка',
                    items: [
                        'examples/components/pages',
                        'examples/components/regions',
                        'examples/components/fieldset',
                    ],
                },
                {
                    type: 'category',
                    label: 'Навигация',
                    items: [
                        'examples/components/menu',
                        'examples/components/pagination',
                    ],
                },
                {
                    type: 'category',
                    label: 'Взаимодействие',
                    items: [
                        'examples/components/modal',
                        'examples/components/alerts',
                        'examples/components/actions',
                    ],
                },
                {
                    type: 'category',
                    label: 'Ввод и вывод данных',
                    items: [
                        'examples/components/fields',
                        'examples/components/output_fields',
                        'examples/components/select',
                        'examples/components/select_tree',
                        'examples/components/date_time',
                        'examples/components/cells',
                    ],
                },
                {
                    type: 'category',
                    label: 'Интерфейс',
                    items: [
                        'examples/components/image',
                        'examples/components/checkbox',
                        'examples/components/radio',
                        'examples/components/progress_bar',
                        'examples/components/slider',
                        'examples/components/switcher',
                        'examples/components/rating',
                    ],
                },
                {
                    type: 'category',
                    label: 'Виджеты',
                    items: [
                        'examples/components/table',
                        'examples/components/chart',
                        'examples/components/cards',
                        'examples/components/form',
                        'examples/components/list',
                        'examples/components/tree',
                        'examples/components/tiles',
                    ],
                },
            ],
        },
        {
            type: 'category',
            label: 'Варианты использования',
            items: [
                'examples/cases/uxcases_input',
                'examples/cases/uxcases_edit_data',
                'examples/cases/uxcases_list',
                'examples/cases/uxcases_list_actions',
                'examples/cases/uxcases_filters',
                'examples/cases/uxcases_table',
            ],
        },
    ],
}
