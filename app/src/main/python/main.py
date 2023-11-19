#import sounddevice
#import speech_recognition as sr
#from deep_translator import GoogleTranslator


def process_speech(text):
# Faça o processamento necessário na fala recebida
    processed_text = text.upper()
    return processed_text + " vamo krl"
'''
def listen_mic(recognizer, microphone, lang='pt-BR'):
    if not isinstance(recognizer, sr.Recognizer):
        raise TypeError("`recognizer` must be `Recognizer` instance")

    if not isinstance(microphone, sr.Microphone):
        raise TypeError("`microphone` must be `Microphone` instance")

    with microphone as source:
        recognizer.adjust_for_ambient_noise(source)
        audio = recognizer.listen(source)

    return audio

def recognize_speech_from_mic(recognizer, microphone, lang='pt-BR', txt='Fala algo'):

    print(txt)
    audio = listen_mic(recognizer, microphone)

    response = {
        "success": True,
        "error": None,
        "transcription": None
    }

    try:
        response["transcription"] = recognizer.recognize_google(audio, language=lang)
    except sr.RequestError:
        response["success"] = False
        response["error"] = "API não disponível"
    except sr.UnknownValueError:
        response["error"] = "Fala não reconhecida"

    return response


def define_language(recognizer, microphone, lang='pt-BR'):

    response = recognize_speech_from_mic(recognizer, microphone, lang, "Escolha uma língua")

    if response["transcription"] in TARGET_LANGUAGES.keys():
        return TARGET_LANGUAGES[response["transcription"]]


TARGET_LANGUAGES = {"russo": "ru-RU", "francês": "fr-FR", "inglês": "en-US", "alemão": "de-DE", "mandarim": "zh-CN", "polonês": "pl", "português": "pt-BR", "espanhol": "es-ES", "japonês": "ja", "coreano": "ko"}

WORDS = {"não":(0,), "sim": (1,), "talvez": (0,1), "tudo": (0,1,1), "certo": (0,0)}


microphone = sr.Microphone()
recognizer = sr.Recognizer()
def_lang = define_language(recognizer, microphone)

while True:

    if def_lang is None:
        def_lang = define_language(recognizer, microphone)
        continue


    phrase = recognize_speech_from_mic(recognizer, microphone, def_lang)

    if phrase["transcription"] is not None:
        translated_text = GoogleTranslator(source='auto', target='pt').translate(phrase["transcription"]).lower()
        if 'sair' in translated_text:
            print("Saindo...")
            break

        intersect = [k for k in translated_text.split() if WORDS.get(k)]
        intersect_sorted = sorted(intersect, key=lambda x: translated_text.split().index(x))

        if intersect_sorted:
            for k in intersect_sorted:
                print(*WORDS.get(k))


    if phrase["error"]:
        print("ERROR: {}".format(phrase["error"]))
        pass
'''