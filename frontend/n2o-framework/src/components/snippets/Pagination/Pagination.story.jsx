import React from 'react'
import PropTypes from 'prop-types'
import { storiesOf, forceReRender } from '@storybook/react'
import { StateDecorator, Store } from '@sambego/storybook-state'

import Pagination from './Pagination'

const initPages = {
    simple: 1,
    prevNext: 1,
    firstLast: 1,
    combination: 1,
}

const store = new Store({
    page: 1,
    ...initPages,
})

store.subscribe(forceReRender)

const stories = storiesOf('UI Компоненты/Пагинация', module)

stories.addDecorator(StateDecorator(store))
stories
    .add(
        'Компонент',
        () => {
            const props = {
                prev: false,
                next: false,
                first: false,
                last: false,
                lazy: false,
                showCountRecords: true,
                hideSinglePage: true,
                activePage: store.get('page'),
                count: 100,
                size: 10,
                maxButtons: 4,
                stepIncrement: 10,
            }

            return (
                <Pagination
                    onSelect={(page) => {
                        store.set({ page })
                    }}
                    activePage={store.get('page')}
                    {...props}
                />
            )
        },
        {
            jsx: { skip: 1 },
            info: {
                text: `
        Компонент 'Пагинация'
        ~~~js
        import Pagination from 'n2o-framework/lib/components/snippets/Pagination/Pagination';
        
        <Pagination
          onSelect={onSelect}
          activePage={activePage}
          prev={false}
          next={false}
          first={false}
          last={false}
          lazy={false}
          showCountRecords
          hideSinglePage
          count={100}
          size={10}
          maxButtons={4}
          stepIncrement={10}
        />
        ~~~
        `,
            },
        },
    )
    .add(
        'Интерактивное использование',
        () => (
            <Pagination
                onSelect={(page) => {
                    store.set({ page })
                }}
                activePage={store.get('page')}
                size={10}
                count={100}
                maxButtons={4}
                stepIncrement={10}
            />
        ),
        {
            jsx: { skip: 1 },
            info: {
                text: `
        Компонент 'Пагинация'
        ~~~js
        import Pagination from 'n2o-framework/lib/components/snippets/Pagination/Pagination';
        
        <Pagination
          onSelect={onSelect}
          activePage={activePage}
          size={10}
          count={100}
          maxButtons={4}
          stepIncrement={10}
        />
        ~~~
        `,
            },
        },
    )
    .add(
        'Вариации',
        () => (
            <>
                <div className="row">
                    <Pagination
                        activePage={store.get('simple')}
                        onSelect={page => store.set({ simple: page })}
                        size={10}
                        count={100}
                        maxButtons={4}
                        stepIncrement={10}
                    />
                </div>
                <div className="row">
                    <Pagination
                        activePage={store.get('prevNext')}
                        onSelect={page => store.set({ prevNext: page })}
                        prev
                        next
                        size={10}
                        count={100}
                        maxButtons={4}
                        stepIncrement={10}
                    />
                </div>
                <div className="row">
                    <Pagination
                        activePage={store.get('firstLast')}
                        onSelect={page => store.set({ firstLast: page })}
                        first
                        last
                        size={10}
                        count={100}
                        maxButtons={4}
                        stepIncrement={10}
                    />
                </div>
                <div className="row">
                    <Pagination
                        activePage={store.get('combination')}
                        onSelect={page => store.set({ combination: page })}
                        prev
                        next
                        first
                        last
                        size={10}
                        count={100}
                        maxButtons={4}
                        stepIncrement={10}
                    />
                </div>
            </>
        ),
        {
            jsx: { skip: 1 },
            info: {
                text: `
        Компонент 'Пагинация'
        ~~~js
        import Pagination from 'n2o-framework/lib/components/snippets/Pagination/Pagination';
        
        <Pagination
            activePage={activePage}
            onSelect={onSelect}
            size={10}
            count={100}
            maxButtons={4}
            stepIncrement={10}
          />
          <Pagination
            activePage={activePage}
            onSelect={onSelect}
            prev={true}
            next={true}
            size={10}
            count={100}
            maxButtons={4}
            stepIncrement={10}
          />
          <Pagination
            activePage={activePage}
            onSelect={onSelect}
            first={true}
            last={true}
            size={10}
            count={100}
            maxButtons={4}
            stepIncrement={10}
          />
          <Pagination
            activePage={activePage}
            onSelect={onSelect}
            prev={true}
            next={true}
            first={true}
            last={true}
            size={10}
            count={100}
            maxButtons={4}
            stepIncrement={10}
          />
        ~~~
        `,
            },
        },
    )
