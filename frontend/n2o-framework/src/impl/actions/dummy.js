export default function dummyImpl({ dispatch, ...options }) {
    // eslint-disable-next-line no-alert
    alert('Ahoy!')
    // eslint-disable-next-line no-console
    console.log(options)
}
