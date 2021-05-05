import React from 'react'
import configureMockStore from 'redux-mock-store'
import { Provider } from 'react-redux'
import { batchActions } from 'redux-batched-actions/lib/index'

import { registerButton } from '../../actions/toolbar'
import { addFieldMessage } from '../../actions/formPlugin'
import { querySelector } from '../widgets/selectors'

import withActionButton from './withActionButton'

const mockStore = configureMockStore()

const delay = ms => new Promise(r => setTimeout(() => r(), ms))

const testProps = {
    id: 'id',
    entityKey: 'form_name',
    conditions: {
        label: 'label',
        icon: 'icon',
        size: 'size',
        color: 'color',
        outline: 'outline',
    },
    confirm: { text: 'test' },
    visible: true,
    disabled: false,
    count: 'count',
}
const NullComponent = ({ onClick, onMouseOver, onMouseEnter }) => (
    <div
        onClick={onClick}
        onMouseOver={onMouseOver}
        onMouseEnter={onMouseEnter}
    />
)
const setup = ({ config, props, state }) => {
    const store = mockStore(state || {})
    const div = document.createElement('div')

    div.setAttribute('id', 'uid')
    document.body.appendChild(div)
    const NullComp = withActionButton(config)(NullComponent)

    const wrapper = mount(
        <Provider store={store}>
            <NullComp {...props} />
        </Provider>,
    )

    return { store, wrapper }
}

describe('<Link />', () => {
    // it('Регистрация кнопки, показ конфирма, запуск валидации', async () => {
    //   const { store, wrapper } = setup({
    //     props: testProps,
    //     config: {
    //       onClick: () => {}
    //     }
    //   });
    //
    //   expect(store.getActions()[0]).toEqual(
    //     registerButton(testProps.entityKey, 'id', {
    //       count: testProps.count,
    //       visible: testProps.visible,
    //       disabled: testProps.disabled,
    //       conditions: testProps.conditions
    //     })
    //   );
    //
    //   expect(wrapper.find('ModalDialog').exists()).toBeTruthy();
    //   await wrapper.find(NullComponent).simulate('click');
    //   wrapper.update();
    //   expect(wrapper.find('ModalDialog').props()).toEqual({
    //     close: expect.any(Function),
    //     onConfirm: expect.any(Function),
    //     onDeny: expect.any(Function),
    //     text: 'test',
    //     visible: true
    //   });
    // });
    // it('Валидация по клику', async () => {
    //   const { store, wrapper } = setup({
    //     props: { ...testProps, validate: true },
    //     config: {
    //       onClick: () => {},
    //     },
    //     state: {
    //       form: {
    //         form_name: {
    //           values: {},
    //           fields: {
    //             name: {},
    //           },
    //         },
    //       },
    //       widgets: {
    //         form_name: {
    //           validation: {
    //             name: [
    //               {
    //                 type: 'required',
    //                 severity: 'danger',
    //                 text: 'Поле обязательно для заполнения',
    //               },
    //             ],
    //           },
    //         },
    //       },
    //     },
    //   });
    //   await wrapper.find(NullComponent).simulate('click');
    //   await delay(400);
    //   console.log(store.getActions())
    //   expect(store.getActions()[1]).toEqual(
    //     batchActions([
    //       addFieldMessage(
    //         'form_name',
    //         'name',
    //         {
    //           severity: 'danger',
    //           text: 'Поле обязательно для заполнения',
    //         },
    //         true
    //       ),
    //     ])
    //   );
    // });
})
