import React from 'react'
import { storiesOf } from '@storybook/react'
import Form from 'reactstrap/lib/Form'
import FormGroup from 'reactstrap/lib/FormGroup'
import Label from 'reactstrap/lib/Label'
import Input from 'reactstrap/lib/Input'

import Filter from './Filter'

const stories = storiesOf('UI Компоненты/Фильтр', module)

stories.addParameters({
    info: {
        propTablesExclude: [Form, FormGroup, Label, Input],
    },
})

stories
    .add(
        'Компонент',
        () => {
            const props = {
                style: {},
                className: 'n2o',
                filters: [],
            }

            return (
                <Filter {...props} onReset={() => {}}>
                    <Form>
                        <FormGroup>
                            <Label for="exampleEmail">Почта</Label>
                            <Input
                                type="email"
                                id="exampleEmail"
                                placeholder="Почта"
                                value=""
                                onChange={(e) => {}}
                            />
                        </FormGroup>
                    </Form>
                </Filter>
            )
        },
        {
            info: {
                text: `
      Компонент 'Фильтр'
      ~~~js
      import Filter from 'n2o-framework/lib/components/snippets/Filter/Filter';
      
      <Filter className="n2o" onReset={onReset}>
        <Form>
          <FormGroup>
            <Label for="exampleEmail">Почта</Label>
            <Input
              type="email"
              id="exampleEmail"
              placeholder="Почта"
              value=""
              onChange={e => {}}
            />
          </FormGroup>
        </Form>
      </Filter>
      ~~~
      `,
            },
        },
    )
    .add(
        'Текст кнопок',
        () => {
            const props = {
                searchLabel: 'Свой текст поиска',
                resetLabel: 'Свой текст сброса',
            }

            return (
                <Filter {...props}>
                    <Form>
                        <FormGroup>
                            <Label for="topInput">Почта</Label>
                            <Input type="email" id="topInput" placeholder="Почта" />
                        </FormGroup>
                    </Form>
                </Filter>
            )
        },
        {
            info: {
                text: `
      Компонент 'Фильтр'
      ~~~js
      import Filter from 'n2o-framework/lib/components/snippets/Filter/Filter';
      
      <Filter 
          searchLabel="Свой текст поиска"
          resetLabel="Свой текст сброса" 
          >
        <Form>
          <FormGroup>
            <Label for="topInput">Почта</Label>
            <Input type="email" id="topInput" placeholder="Почта" />
          </FormGroup>
        </Form>
      </Filter>
      ~~~
      `,
            },
        },
    )
