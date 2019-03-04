package me.zsj.interessant

class RetrofitFactory {

    companion object {
        private var instance: WorkerRetrofit? = null
            get() {
                if (field == null) {
                    field = WorkerRetrofit()
                }
                return field
            }

        fun getRetrofit(): WorkerRetrofit {
            //细心的小伙伴肯定发现了，这里不用getInstance作为为方法名，
            // 是因为在伴生对象声明时，内部已有getInstance方法，所以只能取其他名字
            return instance!!
        }
    }

}