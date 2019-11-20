import ecpPlugin from '../lib/cadesplugin_api';
import Code from './Code';

class EcpApi {
    get error() {
        return this.error;
    }

    set error(error) {
        this.error = error;
    }

    handleError(error) {
        this.error(error);
    }

    getPlugin() {
        ecpPlugin.loadPlugin(this.handleError);
    }

    async hasCertificates () {
        return new Promise((resolve, reject) => {
            this.getPlugin();

            const isIE11 = !navigator.userAgent.match(/Trident.*rv[ :]*11\./);
            if (isIE11) {
                cadesplugin.then(() => {
                        return Code.GetSertListNumber()
                            .then(
                                () => resolve(),
                                (error) => reject(error)
                            );
                    },
                    function (error) {
                        reject(error)
                    }
                )
            } else {
                window.addEventListener("message", (event) => {
                        if (event.data == "cadesplugin_loaded") {
                            return Code.GetSertListNumber().then(() => {
                                resolve();
                            }, function (error) {
                                reject(error);
                            })
                        } else if (event.data == "cadesplugin_load_error") {
                            reject(error)
                        }
                    },
                    false);
                window.postMessage("cadesplugin_echo_request", "*");
            }
        })
    }
}

export default EcpApi;