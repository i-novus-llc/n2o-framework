import React from 'react'
import { storiesOf } from '@storybook/react'

import OutputText from './OutputText'
import OutputJSON from './Output.meta'

const stories = storiesOf('Контролы/OutputText', module)

const props = {
    type: OutputJSON.type,
    textPlace: OutputJSON.textPlace,
    icon: OutputJSON.icon,
    value: 'text',
    format: '',
    disabled: OutputJSON.disabled,
}

const component = (propsOverride = {}, style = { width: 200 }) => (
    <div style={style}>
        <OutputText {...props} {...propsOverride} />
    </div>
)

stories
    .add(
        'Компонент',
        () => component(),
        {
            info: {
                text: `
      Компонент 'Вывод текста'
      ~~~js
      import OutputText from 'n2o-framework/lib/components/controls/OutputText/OutputText';
      
      <OutputText
          className="n2o"
          type="iconAndText"
          textPlace="left"
          icon="fa fa-plus"
          value="text"
          expandable={true}
          ellipsis={true}
      />
      ~~~
      `,
            },
        },
    )
    .add(
        'Длинная строка с переносом',
        () => component({
            value:
          'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aut harum laudantium temporibus! Alias cum fugiat iusto laborum, non officia similique sint vel. At blanditiis, eaque explicabo magni quibusdam quisquam! Sapiente.',
        }),
        {
            info: {
                text: `
      Компонент 'Вывод текста'
      ~~~js
      import OutputText from 'n2o-framework/lib/components/controls/OutputText/OutputText';
      
      <OutputText
          {...props}
          value="Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aut harum laudantium temporibus! Alias cum fugiat iusto laborum, non officia similique sint vel. At blanditiis, eaque explicabo magni quibusdam quisquam! Sapiente."
      />
      ~~~
      `,
            },
        },
    )
    .add(
        'Длинная строка с ellipsis',
        () => component({
            value:
          'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aut harum laudantium temporibus! Alias cum fugiat iusto laborum, non officia similique sint vel. At blanditiis, eaque explicabo magni quibusdam quisquam! Sapiente.',
            ellipsis: true,
        }),
        {
            info: {
                text: `
      Компонент 'Вывод текста'
      ~~~js
      import OutputText from 'n2o-framework/lib/components/controls/OutputText/OutputText';
      
      <OutputText
          {...props}
          ellipsis={true}
          value="Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aut harum laudantium temporibus! Alias cum fugiat iusto laborum, non officia similique sint vel. At blanditiis, eaque explicabo magni quibusdam quisquam! Sapiente."
      />
      ~~~
      `,
            },
        },
    )
    .add(
        '"Подробнее" в конце строки',
        () => component(
            {
                value:
            'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aut harum laudantium temporibus! Alias cum fugiat iusto laborum, non officia similique sint vel. At blanditiis, eaque explicabo magni quibusdam quisquam! Sapiente.',
                expandable: true,
            },
            { width: 400 },
        ),
        {
            info: {
                text: `
      Компонент 'Вывод текста'
      ~~~js
      import OutputText from 'n2o-framework/lib/components/controls/OutputText/OutputText';
      
      <OutputText
          {...props}
          expandable={true}
          value="Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aut harum laudantium temporibus! Alias cum fugiat iusto laborum, non officia similique sint vel. At blanditiis, eaque explicabo magni quibusdam quisquam! Sapiente."
      />
      ~~~
      `,
            },
        },
    )
    .add(
        '"Подробнее" по количеству символов',
        () => component({
            value:
          'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aut harum laudantium temporibus! Alias cum fugiat iusto laborum, non officia similique sint vel. At blanditiis, eaque explicabo magni quibusdam quisquam! Sapiente.',
            expandable: 40,
        }),
        {
            info: {
                text: `
      Компонент 'Вывод текста'
      ~~~js
      import OutputText from 'n2o-framework/lib/components/controls/OutputText/OutputText';
      
      <OutputText
          {...props}
          expandable={40}
          value="Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aut harum laudantium temporibus! Alias cum fugiat iusto laborum, non officia similique sint vel. At blanditiis, eaque explicabo magni quibusdam quisquam! Sapiente."
      />
      ~~~
      `,
            },
        },
    )
