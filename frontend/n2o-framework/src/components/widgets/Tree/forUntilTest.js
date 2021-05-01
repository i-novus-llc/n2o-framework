import React from 'react'

export const Component = () => null

export const toCollectionObject = {
    1: {
        children: [],
        icon: false,
        id: 1,
        key: 1,
        title: (
            <Component
                data={{ id: 1 }}
                datasource={[{ id: 1 }, { id: 2 }, { id: 3 }]}
                valueFieldId="id"
            />
        ),
    },
    2: {
        children: [],
        icon: false,
        id: 2,
        key: 2,
        title: (
            <Component
                data={{ id: 2 }}
                datasource={[{ id: 1 }, { id: 2 }, { id: 3 }]}
                valueFieldId="id"
            />
        ),
    },
    3: {
        children: [],
        icon: false,
        id: 3,
        key: 3,
        title: (
            <Component
                data={{ id: 3 }}
                datasource={[{ id: 1 }, { id: 2 }, { id: 3 }]}
                valueFieldId="id"
            />
        ),
    },
}

export const linerTree = [
    {
        children: [],
        icon: false,
        id: 1,
        key: 1,
        title: (
            <Component
                data={{ id: 1 }}
                datasource={[{ id: 1 }, { id: 2 }, { id: 3 }]}
                parentFieldId="parent"
                valueFieldId="id"
            />
        ),
    },
    {
        children: [],
        icon: false,
        id: 2,
        key: 2,
        title: (
            <Component
                data={{ id: 2 }}
                datasource={[{ id: 1 }, { id: 2 }, { id: 3 }]}
                parentFieldId="parent"
                valueFieldId="id"
            />
        ),
    },
    {
        children: [],
        icon: false,
        id: 3,
        key: 3,
        title: (
            <Component
                data={{ id: 3 }}
                datasource={[{ id: 1 }, { id: 2 }, { id: 3 }]}
                parentFieldId="parent"
                valueFieldId="id"
            />
        ),
    },
]

export const tree = [
    {
        children: [
            {
                children: [],
                icon: false,
                id: 2,
                key: 2,
                parent: 1,
                title: (
                    <Component
                        data={{ id: 2, parent: 1 }}
                        datasource={[{ id: 1 }, { id: 2, parent: 1 }, { id: 3, parent: 1 }]}
                        parentFieldId="parent"
                        valueFieldId="id"
                    />
                ),
            },
            {
                children: [],
                icon: false,
                id: 3,
                key: 3,
                parent: 1,
                title: (
                    <Component
                        data={{ id: 3, parent: 1 }}
                        datasource={[{ id: 1 }, { id: 2, parent: 1 }, { id: 3, parent: 1 }]}
                        parentFieldId="parent"
                        valueFieldId="id"
                    />
                ),
            },
        ],
        icon: false,
        id: 1,
        key: 1,
        title: (
            <Component
                data={{ id: 1 }}
                datasource={[{ id: 1 }, { id: 2, parent: 1 }, { id: 3, parent: 1 }]}
                parentFieldId="parent"
                valueFieldId="id"
            />
        ),
    },
]
